package com.novomind.jira.model;

public enum JiraField {

  SUMMARY("this is a summary"),
  STATUS("new status"),
/*  ISSUE_TYPE(JiraIssueFields::getIssuetype),
  DESCRIPTION(JiraIssueFields::getDescription),
  PRIORITY(JiraIssueFields::getPriority),
  PROJECT(JiraIssueFields::getProject),
  RESOLUTION(JiraIssueFields::getResolution),
  LABELS(JiraIssueFields::getLabels),
  VERSIONS(JiraIssueFields::getVersions),
  FIX_VERSIONS(JiraIssueFields::getFixVersions),
  COMPONENTS(JiraIssueFields::getComponents),
  ASSIGNEE(JiraIssueFields::getAssignee),
  UPDATED(JiraIssueFields::getUpdated),
  COMMENT(JiraIssueFields::getComment),*/
  ;

  private final String fieldName;

/*  WellKnownJiraField(PropertyGetter<JiraIssueFields> getter) {
    this.fieldName = PropertyUtils.getPropertyName(JiraIssueFields.class, getter);
  }*/

  JiraField(String fieldName){
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }

}
