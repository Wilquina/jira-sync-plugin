package com.novomind.jira.crud;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.label.Label;
import com.atlassian.jira.issue.label.LabelManager;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.novomind.jira.model.JiraComment;
import com.novomind.jira.model.JiraIssueHistoryItem;

@Named
public class IssueDataWriter {

  private static final Logger LOG = LoggerFactory.getLogger(IssueDataWriter.class);
  public static final IssueService ISSUE_SERVICE = ComponentAccessor.getIssueService();
  public static final IssueManager ISSUE_MANAGER = ComponentAccessor.getIssueManager();
  public static final CommentManager COMMENT_MANAGER = ComponentAccessor.getCommentManager();

  public void updateIssueSummary(Long destinationIssueId, String newSummary, ApplicationUser user) {
    IssueInputParameters issueInputParameters = ISSUE_SERVICE.newIssueInputParameters();
    issueInputParameters.setSummary(newSummary);
    updateField(destinationIssueId, user, issueInputParameters);
  }

  public void updateIssueDescription(Long destinationIssueId, String newDescription, ApplicationUser user) {
    IssueInputParameters issueInputParameters = ISSUE_SERVICE.newIssueInputParameters();
    issueInputParameters.setDescription(newDescription);
    updateField(destinationIssueId, user, issueInputParameters);
  }

  public void updateIssueResolution(Long destinationIssueId, JiraIssueHistoryItem jiraIssueHistoryItem, ApplicationUser user) {
    String toString = jiraIssueHistoryItem.getToString();
    String to = jiraIssueHistoryItem.getTo();
    if (StringUtils.isNotBlank(toString) && StringUtils.isNotBlank(to) && "Fertig".equals(toString)) {
      IssueInputParameters issueInputParameters = ISSUE_SERVICE.newIssueInputParameters();
      issueInputParameters.setResolutionId(to);
      updateField(destinationIssueId, user, issueInputParameters);
    } else {
      LOG.error("No resolution updated");
    }
  }

  public void updateIssueStatus(Long destinationIssueId, JiraIssueHistoryItem jiraIssueHistoryItem, ApplicationUser user) {
    String toString = jiraIssueHistoryItem.getToString();
    String to = jiraIssueHistoryItem.getTo();
    if (StringUtils.isNotBlank(toString) && StringUtils.isNotBlank(to) && "Done".equals(toString)) {
      IssueInputParameters issueInputParameters = ISSUE_SERVICE.newIssueInputParameters();
      issueInputParameters.setStatusId(to);
      updateField(destinationIssueId, user, issueInputParameters);
    } else {
      LOG.error("No status updated");
    }
  }

  public void updateIssueLabels(Long sourceIssueId,
      Long destinationIssueId,
      ApplicationUser user) {
    // TODO Try to get the labels from the webhook JSON instead, need to find out how though
    LabelManager labelManager = ComponentAccessor.getComponent(LabelManager.class);
    MutableIssue issue = ISSUE_MANAGER.getIssueObject(sourceIssueId);
    Set<String> labels = new HashSet<>();
    if (issue != null && issue.getLabels() != null) {
      IssueService.IssueResult issueResult = null;
      for (Label label : issue.getLabels()) {
        if (label.getLabel().equals("clone-to-isbo")) {
          issueResult = cloneIssue(sourceIssueId, user);
        } else {
          labels.add(label.getLabel());
        }
      }
      if (issueResult != null) {
        destinationIssueId = issueResult.getIssue().getId();
      }
      labelManager.setLabels(user, sourceIssueId, labels, false, false);
      labelManager.setLabels(user, destinationIssueId, labels, false, false);
    }
  }

  public void writeOrUpdateComment(Long destinationIssueId, JiraComment jiraComment, ApplicationUser user) {
    String commentBody = jiraComment.getBody();
    if (StringUtils.isNotBlank(commentBody)) {
      MutableIssue issue = ISSUE_MANAGER.getIssueObject(destinationIssueId);
      // If the created date is equal to the updated date, then the comment was just created, else it was updated
      Comment comment = COMMENT_MANAGER.getCommentById(Long.valueOf(jiraComment.getId()));
      if (jiraComment.getDateCreated().compareTo(jiraComment.getDateUpdated()) == 0) {
        COMMENT_MANAGER.create(issue, user, commentBody, null, comment.getRoleLevelId(), false);
      } else {
        // TODO this will not work, as the IDs are different between issues. Need to find a good way to map them
        COMMENT_MANAGER.update(comment, false);
      }
    }
  }

  private IssueService.IssueResult cloneIssue(Long sourceIssueId, ApplicationUser user) {
    ComponentAccessor.getJiraAuthenticationContext().setLoggedInUser(user);
    ApplicationUser loggedInUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    MutableIssue issue = ISSUE_MANAGER.getIssueObject(sourceIssueId);
    IssueInputParameters issueInputParameters = ISSUE_SERVICE.newIssueInputParameters();
    Project project = ComponentAccessor.getProjectManager().getProjectByCurrentKey("DP");
    issueInputParameters.setSummary(issue.getSummary())
        .setDescription(issue.getDescription())
        .setAssigneeId(loggedInUser.getName())
        .setReporterId(loggedInUser.getName())
        .setProjectId(project.getId())
        .setIssueTypeId(StringUtils.isNotBlank(issue.getIssueTypeId()) ? issue.getIssueTypeId() : "10000");

    IssueService.CreateValidationResult result = ISSUE_SERVICE.validateCreate(user, issueInputParameters);
    IssueService.IssueResult issueResult = null;
    if (result.isValid()) {
      issueResult = ISSUE_SERVICE.create(user, result);
    } else if (result.getErrorCollection().hasAnyErrors()) {
      LOG.error("There were errors validating the issue creation");
      result.getErrorCollection().getErrors().forEach(LOG::error);
      result.getErrorCollection().getErrorMessages().forEach(LOG::error);
      result.getErrorCollection().getReasons().forEach(reason -> LOG.error(reason.toString() + " " + reason.getHttpStatusCode()));
    }
    return issueResult;
  }

  private void updateField(Long destinationIssueId,
      ApplicationUser user,
      IssueInputParameters issueInputParameters) {
    IssueService.UpdateValidationResult updateValidationResult =
        IssueDataWriter.ISSUE_SERVICE.validateUpdate(user, destinationIssueId, issueInputParameters);
    if (updateValidationResult.isValid()) {
      LOG.info("Update has been successfully validated. Applying update.");
      IssueService.IssueResult updateResult = IssueDataWriter.ISSUE_SERVICE.update(user, updateValidationResult);
      if (!updateResult.isValid()) {
        LOG.error("Update result was invalid.");
      }
    } else {
      LOG.error("Update validation was unsuccessful.");
      updateValidationResult.getErrorCollection().getErrorMessages().forEach(LOG::error);
      updateValidationResult.getWarningCollection().getWarnings().forEach(LOG::warn);
    }
  }
}
