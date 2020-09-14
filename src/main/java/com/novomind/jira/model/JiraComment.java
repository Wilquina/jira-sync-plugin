package com.novomind.jira.model;

import org.joda.time.DateTime;

public class JiraComment extends JiraIdResource {

  private String id;
  private String body;
  private DateTime dateCreated;
  private DateTime dateUpdated;
  private String visibilityType;
  private String visibilityValue;

  public JiraComment() {
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public DateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(DateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  public DateTime getDateUpdated() {
    return dateUpdated;
  }

  public void setDateUpdated(DateTime dateUpdated) {
    this.dateUpdated = dateUpdated;
  }

  public String getVisibilityType() {
    return visibilityType;
  }

  public void setVisibilityType(String visibilityType) {
    this.visibilityType = visibilityType;
  }

  public String getVisibilityValue() {
    return visibilityValue;
  }

  public void setVisibilityValue(String visibilityValue) {
    this.visibilityValue = visibilityValue;
  }
}
