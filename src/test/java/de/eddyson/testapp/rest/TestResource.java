package de.eddyson.testapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class TestResource {

  public TestResource() { }

  @GET
  @Path("/test")
  @Produces(MediaType.APPLICATION_JSON)
  public Response test() {
    return Response.ok("ok").build();
  }
}
