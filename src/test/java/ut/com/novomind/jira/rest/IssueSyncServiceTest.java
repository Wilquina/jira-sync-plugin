package ut.com.novomind.jira.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.novomind.jira.crud.IssueUpdater;
import com.novomind.jira.model.IssueSyncServiceModel;
import com.novomind.jira.rest.IssueSyncService;

public class IssueSyncServiceTest {

  @Before
  public void setup() {

  }

  @After
  public void tearDown() {

  }

//  @Test
//  public void messageIsValid() {
//    IssueSyncService resource = new IssueSyncService(new IssueUpdater());
//
//    Response response = resource.getMessage();
//    final IssueSyncServiceModel message = (IssueSyncServiceModel) response.getEntity();
//
//    assertEquals("wrong message", "Hello World", message.getMessage());
//  }
}
