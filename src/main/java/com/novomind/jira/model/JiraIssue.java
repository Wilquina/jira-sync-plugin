package com.novomind.jira.model;

import java.util.ArrayList;
import java.util.List;

public class JiraIssue extends JiraIdResource {

  private static final long serialVersionUID = 1L;

  private String key;
  private List<String> labels;

  public JiraIssue() {
    this.labels = new ArrayList<>();
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public List<String> getLabels() {
    return labels;
  }

  public void setLabels(List<String> labels) {
    this.labels = labels;
  }
}
