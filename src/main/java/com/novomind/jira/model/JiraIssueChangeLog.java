package com.novomind.jira.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JiraIssueChangeLog implements Serializable {

  private static final long serialVersionUID = 1L;

  private int total;

  private List<JiraIssueHistoryEntry> histories;

  public JiraIssueChangeLog() {
    this.histories = new ArrayList<>();
  }

  public JiraIssueChangeLog(List<JiraIssueHistoryEntry> histories) {
    this.histories = histories;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public List<JiraIssueHistoryEntry> getHistories() {
    return histories;
  }

  public void setHistories(List<JiraIssueHistoryEntry> histories) {
    this.histories = histories;
  }

  @JsonIgnore
  public JiraIssueChangeLog addHistoryEntry(JiraIssueHistoryEntry historyEntry) {
    getHistories().add(historyEntry);
    return this;
  }

  @JsonIgnore
  public JiraIssueHistoryEntry getLatestStatusTransition() {
    return histories.stream()
        .filter(historyEntry -> historyEntry.hasItemWithField(JiraField.STATUS))
        .max(Comparator.comparing(JiraIssueHistoryEntry::getCreated))
        .orElse(null);
  }
}
