package com.novomind.jira.model;

public class JiraIssueFields extends JiraResource implements JiraFieldsBean {

  private String summary;

  public JiraIssueFields() {
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }
}
