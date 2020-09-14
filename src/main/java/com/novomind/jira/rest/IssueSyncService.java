package com.novomind.jira.rest;

import static org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.novomind.jira.crud.IssueUpdater;
import com.novomind.jira.model.JiraIssueUpdateModel;

@Path("/")
@AnonymousAllowed
@Named
public class IssueSyncService {

  private static final Logger LOG = LoggerFactory.getLogger(IssueSyncService.class);

  @Inject
  private IssueUpdater issueUpdater;

  @POST
  @Path("notify")
  @AnonymousAllowed
  @Consumes(MediaType.APPLICATION_JSON)
  public Response notifyIssueChanges(String json) {
    try {
      LOG.error(json);
      ObjectMapper mapper = new ObjectMapper()
          .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
      issueUpdater.updateLinkedIssue(mapper.readValue(json, JiraIssueUpdateModel.class));
    } catch (IOException e) {
      LOG.error("There was an error updating the issues", e);
      return Response.serverError().build();
    }
    return Response.ok().build();
  }
}