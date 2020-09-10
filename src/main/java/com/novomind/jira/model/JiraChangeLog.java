package com.novomind.jira.model;

import java.io.Serializable;
import java.util.List;

public class JiraChangeLog implements Serializable {

  private static final long serialVersionUID = 1L;

  public JiraChangeLog() {
  }

  private List<JiraIssueHistoryItem> items;

  public List<JiraIssueHistoryItem> getItems() {
    return items;
  }

  public void setItems(List<JiraIssueHistoryItem> items) {
    this.items = items;
  }
}
