package com.novomind.jira.crud;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.label.LabelManager;
import com.atlassian.jira.user.ApplicationUser;
import com.novomind.jira.model.JiraComment;
import com.novomind.jira.model.JiraIssueHistoryItem;

@Named
public class IssueDataWriter {

  private static final Logger LOG = LoggerFactory.getLogger(IssueDataWriter.class);
  public static final IssueService ISSUE_SERVICE = ComponentAccessor.getIssueService();
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
    }
  }

  public void updateIssueStatus(Long destinationIssueId, JiraIssueHistoryItem jiraIssueHistoryItem, ApplicationUser user) {
    String toString = jiraIssueHistoryItem.getToString();
    String to = jiraIssueHistoryItem.getTo();
    if (StringUtils.isNotBlank(toString) && StringUtils.isNotBlank(to) && "Done".equals(toString)) {
      IssueInputParameters issueInputParameters = ISSUE_SERVICE.newIssueInputParameters();
      issueInputParameters.setStatusId(to);
      updateField(destinationIssueId, user, issueInputParameters);
    }
  }

  public void updateIssueLabels(Long sourceIssueId, Long destinationIssueId, ApplicationUser user) {
    // TODO Try to get the labels from the webhook JSON instead, need to find out how though
    LabelManager labelManager = ComponentAccessor.getComponent(LabelManager.class);
    MutableIssue issue = ComponentAccessor.getIssueManager().getIssueObject(sourceIssueId);
    if (issue != null && issue.getLabels() != null) {
      issue.getLabels().forEach(
          label -> labelManager.addLabel(user, destinationIssueId, label.getLabel(), false)
      );
    }
  }

  public void writeOrUpdateComment(Long destinationIssueId, JiraComment jiraComment, ApplicationUser user) {
    String commentBody = jiraComment.getBody();
    if (StringUtils.isNotBlank(commentBody)) {
      MutableIssue issue = ComponentAccessor.getIssueManager().getIssueObject(destinationIssueId);
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

  private void updateField(Long destinationIssueId,
      ApplicationUser user,
      IssueInputParameters issueInputParameters) {
    IssueService.UpdateValidationResult updateValidationResult =
        IssueDataWriter.ISSUE_SERVICE.validateUpdate(user, destinationIssueId, issueInputParameters);
    if (updateValidationResult.isValid()) {
      LOG.info("Update has been successfully validated. Applying update.");
      IssueService.IssueResult updateResult = IssueDataWriter.ISSUE_SERVICE.update(user, updateValidationResult);
      if (!updateResult.isValid()) {
        LOG.warn("Update result was invalid.");
      }
    } else {
      LOG.warn("Update validation was unsuccessful.");
      updateValidationResult.getErrorCollection().getErrorMessages().forEach(LOG::error);
      updateValidationResult.getWarningCollection().getWarnings().forEach(LOG::warn);
    }
  }
}
