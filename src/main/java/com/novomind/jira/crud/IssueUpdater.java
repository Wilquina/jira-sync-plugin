package com.novomind.jira.crud;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.link.IssueLink;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.user.ApplicationUser;
import com.novomind.jira.model.JiraIssueHistoryItem;
import com.novomind.jira.model.JiraIssueUpdateModel;

@Named
public class IssueUpdater {

  private static final Logger LOG = LoggerFactory.getLogger(IssueUpdater.class);

  @Inject
  private IssueDataWriter issueDataWriter;

  public void updateLinkedIssue(JiraIssueUpdateModel jiraIssueUpdateModel) {
    Long sourceIssueId = NumberUtils.toLong(jiraIssueUpdateModel.getJiraIssue().getId(), 0L);
    Long destinationIssueId = getLinkedIssueId(sourceIssueId);
    ApplicationUser loggedInuser = ComponentAccessor.getUserManager().getUserByKey(jiraIssueUpdateModel.getJiraUser().getKey());
    ApplicationUser user = ComponentAccessor.getUserManager().getUserByKey("cschiess");
    List<JiraIssueHistoryItem> items = jiraIssueUpdateModel.getJiraChangeLog().getItems();
    if (!items.isEmpty()) {
      items.forEach(
          item -> {
            String field = item.getField();
            if ("summary".equals(field)) {
              issueDataWriter.updateIssueSummary(destinationIssueId, item.getToString(), user);
            } else if ("description".equals(field)) {
              issueDataWriter.updateIssueDescription(destinationIssueId, item.getToString(), user);
            } else if ("resolution".equals(field)) {
              issueDataWriter.updateIssueResolution(destinationIssueId, item, user);
            } else if ("status".equals(field)) {
              issueDataWriter.updateIssueStatus(destinationIssueId, item, user);
            }
          }
      );
    }
    issueDataWriter.updateIssueLabels(sourceIssueId, destinationIssueId, loggedInuser);
    issueDataWriter.writeOrUpdateComment(destinationIssueId, jiraIssueUpdateModel.getJiraComment(), user);
  }

  private Long getLinkedIssueId(Long sourceIssueId) {
    IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager();
    List<IssueLink> outwardLinks = issueLinkManager.getOutwardLinks(sourceIssueId);
    List<IssueLink> inwardLinks = issueLinkManager.getInwardLinks(sourceIssueId);
    // TODO maybe get the issueLinks directly from the webhook response
    if (outwardLinks != null && !outwardLinks.isEmpty()) {
      return outwardLinks.get(0).getDestinationId();
    } else if (inwardLinks != null && !inwardLinks.isEmpty()) {
      return inwardLinks.get(0).getSourceId();
    }
    return 0L;
  }
}
