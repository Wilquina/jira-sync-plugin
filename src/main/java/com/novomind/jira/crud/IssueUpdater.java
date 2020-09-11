package com.novomind.jira.crud;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.link.IssueLink;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.user.ApplicationUser;
import com.novomind.jira.model.JiraIssueUpdateModel;

@Named
public class IssueUpdater {

  @Inject
  private IssueDataWriter issueDataWriter;

  public void updateLinkedIssue(JiraIssueUpdateModel jiraIssueUpdateModel) {
    Long sourceIssueId = NumberUtils.toLong(jiraIssueUpdateModel.getJiraIssue().getId(), 0L);
    Long destinationIssueId = getLinkedIssueId(sourceIssueId);
    ApplicationUser user = ComponentAccessor.getUserManager().getUserByKey(jiraIssueUpdateModel.getJiraUser().getKey());
    jiraIssueUpdateModel.getJiraChangeLog().getItems().forEach(
        item -> {
          if ("summary".equals(item.getField())) {
            issueDataWriter.updateIssueSummary(destinationIssueId, item.getToString(), user);
          } else if ("labels".equals(item.getField())) {
            issueDataWriter.updateIssueLabels(sourceIssueId, destinationIssueId, user);
          } else if ("description".equals(item.getField())) {
            issueDataWriter.updateIssueDescription(destinationIssueId, item.getToString(), user);
          }
        }
    );
    if(StringUtils.isNotBlank(jiraIssueUpdateModel.getJiraComment().getBody())){
      issueDataWriter.writeOrUpdateComment();
    }
  }

  private Long getLinkedIssueId(Long sourceIssueId) {
    IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager();
    List<IssueLink> outwardLinks = issueLinkManager.getOutwardLinks(sourceIssueId);
    List<IssueLink> inwardLinks = issueLinkManager.getInwardLinks(sourceIssueId);
    if (outwardLinks != null) {
      return outwardLinks.get(0).getDestinationId();
    } else if (inwardLinks != null) {
      return inwardLinks.get(0).getDestinationId();
    }
    return 0L;
  }
}
