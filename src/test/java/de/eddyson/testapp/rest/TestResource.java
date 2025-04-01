package de.eddyson.testapp.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
