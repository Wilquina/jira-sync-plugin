package com.novomind.jira.rest;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdDeserializer;

import com.novomind.jira.model.JiraChangeLog;
import com.novomind.jira.model.JiraIssueHistoryItem;

public class JiraChangeLogDeserializer extends StdDeserializer<JiraChangeLog> {

  public JiraChangeLogDeserializer() {
    this(null);
  }

  public JiraChangeLogDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public JiraChangeLog deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    Root root = jsonParser.readValueAs(Root.class);
    JiraChangeLog jiraChangeLog = new JiraChangeLog();
    if(root != null){
      jiraChangeLog.setItems(root.items);
    }
    return null;
  }

  private static class Root {
    public List<JiraIssueHistoryItem> items;
  }
}
