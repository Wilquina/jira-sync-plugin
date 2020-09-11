package com.novomind.jira.model;

public class JiraUser {

  private String key;
  private String name;

  public JiraUser() {
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
