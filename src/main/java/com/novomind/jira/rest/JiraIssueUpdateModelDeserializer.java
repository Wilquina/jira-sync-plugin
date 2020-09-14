package com.novomind.jira.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.joda.time.DateTime;

import com.novomind.jira.model.JiraChangeLog;
import com.novomind.jira.model.JiraComment;
import com.novomind.jira.model.JiraIssue;
import com.novomind.jira.model.JiraIssueHistoryItem;
import com.novomind.jira.model.JiraIssueUpdateModel;
import com.novomind.jira.model.JiraUser;

public class JiraIssueUpdateModelDeserializer extends StdDeserializer<JiraIssueUpdateModel> {

  public JiraIssueUpdateModelDeserializer() {
    this(null);
  }

  public JiraIssueUpdateModelDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public JiraIssueUpdateModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    Root root = jsonParser.readValueAs(Root.class);
    JiraIssueUpdateModel jiraIssueUpdateModel = new JiraIssueUpdateModel();
    if (root != null) {
      jiraIssueUpdateModel.setEvent(root.webhookEvent);
      jiraIssueUpdateModel.setId(root.id);
      jiraIssueUpdateModel.setTimestamp(root.timestamp);
      setJiraIssue(root, jiraIssueUpdateModel);
      setJiraUser(root, jiraIssueUpdateModel);
      setJiraIssueHistoryItems(root, jiraIssueUpdateModel);
      setJiraComment(root, jiraIssueUpdateModel);
    }
    return jiraIssueUpdateModel;
  }

  private void setJiraIssue(Root root, JiraIssueUpdateModel jiraIssueUpdateModel) {
    JiraIssue jiraIssue = new JiraIssue();
    Issue issue = root.issue;
    if (issue != null) {
      jiraIssue.setId(issue.id);
      jiraIssue.setKey(issue.key);
      jiraIssue.setLabels(issue.labels);
    }
    jiraIssueUpdateModel.setJiraIssue(jiraIssue);
  }

  private void setJiraUser(Root root, JiraIssueUpdateModel jiraIssueUpdateModel) {
    JiraUser jiraUser = new JiraUser();
    User user = root.user;
    if (user != null) {
      jiraUser.setKey(user.key);
      jiraUser.setName(user.name);
    }
    jiraIssueUpdateModel.setJiraUser(jiraUser);
  }

  private void setJiraIssueHistoryItems(Root root, JiraIssueUpdateModel jiraIssueUpdateModel) {
    JiraChangeLog jiraChangeLog = new JiraChangeLog();
    List<JiraIssueHistoryItem> jiraIssueHistoryItems = new ArrayList<>();
    Changelog changelog = root.changelog;
    if (changelog != null && changelog.items != null) {
      changelog.items.forEach(
          item -> {
            JiraIssueHistoryItem jiraIssueHistoryItem = new JiraIssueHistoryItem();
            jiraIssueHistoryItem.setField(item.field);
            jiraIssueHistoryItem.setFromString(item.fromString);
            jiraIssueHistoryItem.setToString(item.toString);
            jiraIssueHistoryItems.add(jiraIssueHistoryItem);
          }
      );
      jiraChangeLog.setItems(jiraIssueHistoryItems);
      jiraIssueUpdateModel.setJiraChangeLog(jiraChangeLog);
    }
  }

  private void setJiraComment(Root root, JiraIssueUpdateModel jiraIssueUpdateModel) {
    JiraComment jiraComment = new JiraComment();
    Comment comment = root.comment;
    if (comment != null) {
      jiraComment.setId(comment.id);
      jiraComment.setBody(comment.body);
      jiraComment.setDateCreated(DateTime.parse(comment.created));
      jiraComment.setDateUpdated(DateTime.parse(comment.updated));
      Visibility visibility = comment.visibility;
      if (visibility != null) {
        jiraComment.setVisibilityType(visibility.type);
        jiraComment.setVisibilityValue(visibility.value);
      }
    }
    jiraIssueUpdateModel.setJiraComment(jiraComment);
  }

  private static class Root {
    public String webhookEvent;
    public int id;
    public long timestamp;
    public Issue issue;
    public User user;
    public Changelog changelog;
    public Comment comment;
  }

  private static class Issue {
    public String id;
    public String key;
    public List<String> labels;
  }

  private static class User {
    public String key;
    public String name;
  }

  private static class Changelog {
    public List<Item> items;
  }

  private static class Item {
    public String field;
    public String fromString;
    public String toString;
  }

  private static class Comment {
    public String id;
    public String body;
    public String created;
    public String updated;
    public Visibility visibility;
  }

  private static class Visibility {
    public String type;
    public String value;
  }
}
