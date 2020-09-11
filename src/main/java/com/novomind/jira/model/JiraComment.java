package com.novomind.jira.model;

public class JiraComment extends JiraIdResource {

  private String id;
  private String body;

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
}
