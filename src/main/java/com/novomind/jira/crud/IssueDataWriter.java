package com.novomind.jira.crud;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.label.LabelManager;
import com.atlassian.jira.user.ApplicationUser;

@Named
public class IssueDataWriter {

  private static final Logger LOG = LoggerFactory.getLogger(IssueDataWriter.class);
  public static final IssueService ISSUE_SERVICE = ComponentAccessor.getIssueService();

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

  public void writeOrUpdateComment(Long destinationIssueId, String comment, ApplicationUser user) {
    // TODO get the projectRole for which the comment should be added
    MutableIssue issue = ComponentAccessor.getIssueManager().getIssueObject(destinationIssueId);
    ComponentAccessor.getCommentManager().create(issue, user, comment, false);
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
