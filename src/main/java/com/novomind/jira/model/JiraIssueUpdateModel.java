package com.novomind.jira.model;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.novomind.jira.rest.JiraIssueUpdateModelDeserializer;

@JsonDeserialize(using = JiraIssueUpdateModelDeserializer.class)
public class JiraIssueUpdateModel {

  private String event;
  private int id;
  private long timestamp;
  private JiraIssue jiraIssue;
  private JiraUser jiraUser;
  private JiraChangeLog jiraChangeLog;
  private JiraComment jiraComment;

  public JiraIssueUpdateModel() {

  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public JiraIssue getJiraIssue() {
    return jiraIssue;
  }

  public void setJiraIssue(JiraIssue jiraIssue) {
    this.jiraIssue = jiraIssue;
  }

  public JiraUser getJiraUser() {
    return jiraUser;
  }

  public void setJiraUser(JiraUser jiraUser) {
    this.jiraUser = jiraUser;
  }

  public JiraChangeLog getJiraChangeLog() {
    return jiraChangeLog;
  }

  public void setJiraChangeLog(JiraChangeLog jiraChangeLog) {
    this.jiraChangeLog = jiraChangeLog;
  }

  public JiraComment getJiraComment() {
    return jiraComment;
  }

  public void setJiraComment(JiraComment jiraComment) {
    this.jiraComment = jiraComment;
  }
}
