package com.novomind.jira.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JiraChangeLog implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<JiraIssueHistoryItem> items;

  public JiraChangeLog() {
    this.items = new ArrayList<>();
  }

  public List<JiraIssueHistoryItem> getItems() {
    return items;
  }

  public void setItems(List<JiraIssueHistoryItem> items) {
    this.items = items;
  }
}
