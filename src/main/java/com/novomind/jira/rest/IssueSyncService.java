package com.novomind.jira.rest;

import static org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.io.IOException;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.novomind.jira.crud.IssueUpdater;
import com.novomind.jira.model.IssueSyncServiceModel;
import com.novomind.jira.model.JiraIssueUpdateModel;

@Path("/")
@AnonymousAllowed
public class IssueSyncService {

  @Nonnull
  private final IssueUpdater issueUpdater;

  public IssueSyncService(@Nonnull IssueUpdater issueUpdater) {
    this.issueUpdater = Objects.requireNonNull(issueUpdater);
  }

  @GET
  @Path("message/{key}")
  @AnonymousAllowed
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response getMessage(@PathParam("key") String key) {
    return Response.ok(new IssueSyncServiceModel(issueUpdater.getUpdateValue(key))).build();
  }

  @POST
  @Path("post")
  @AnonymousAllowed
  @Consumes(MediaType.APPLICATION_JSON)
  public Response notifyIssueChanges(String json) {
    try {
      ObjectMapper mapper = new ObjectMapper()
          .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
      issueUpdater.updateLinkedIssue(mapper.readValue(json, JiraIssueUpdateModel.class));
    } catch (IOException e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
    return Response.ok().build();
  }
}