package com.novomind.jira.model;

import java.io.Serializable;

public class JiraIssueHistoryItem implements Serializable {

  private static final long serialVersionUID = 1L;

  private String field;
  private String fromString;
  private String toString;
  private String from;
  private String to;

  public JiraIssueHistoryItem() {
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getFromString() {
    return fromString;
  }

  public void setFromString(String fromString) {
    this.fromString = fromString;
  }

  public String getToString() {
    return toString;
  }

  public void setToString(String toString) {
    this.toString = toString;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }
}
