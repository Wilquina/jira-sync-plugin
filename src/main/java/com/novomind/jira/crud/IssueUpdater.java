package com.novomind.jira.crud;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import com.google.common.annotations.VisibleForTesting;
import com.novomind.jira.model.JiraIssue;
import com.novomind.jira.model.JiraIssueUpdateModel;

@Named
public class IssueUpdater {

  private Map<String, String> updates = new HashMap<>();

  @VisibleForTesting
  public IssueUpdater() {
    // empty
  }

  public void updateLinkedIssue(JiraIssueUpdateModel jiraIssueUpdateModel) {

    jiraIssueUpdateModel.getJiraChangeLog().getItems().forEach(
        jiraIssueHistoryItem -> updates.put(jiraIssueHistoryItem.getField(), jiraIssueHistoryItem.getToString())
    );

    //    JiraIssue getLinkedIsue = getLinkedIssue(jiraIssueUpdateModel);
    //    IssueService issueService = ComponentAccessor.getIssueService();
    //    jiraIssueUpdateModel.getJiraChangeLog().getHistories().forEach(
    //        jiraIssueHistoryEntry -> jiraIssueHistoryEntry.getItems().forEach(
    //            jiraIssueHistoryItem -> {
    //              if (jiraIssueHistoryItem.getField().equals("summary")) {
    //
    //              }
    //            }
    //        )
    //    );
  }

  public String getUpdateValue(String key) {
    return updates.get(key);
  }

  private JiraIssue getLinkedIssue(JiraIssueUpdateModel jiraIssueUpdateModel) {
    return null;
  }
}
