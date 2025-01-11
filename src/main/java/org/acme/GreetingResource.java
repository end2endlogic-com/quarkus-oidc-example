package org.acme;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello, ";
    }

    @GET
    @Path("/authenticated")
    @Produces(MediaType.TEXT_PLAIN)
    @Authenticated
    public String authenticated() {
        return "Hello, ";
    }

    @Path("/user")
    @GET
    @RolesAllowed({ "user" })
    @Produces(MediaType.TEXT_PLAIN)
    public String user() {
        return "user role, ";
    }

    @Path("/admin")
    @GET
    @RolesAllowed({ "admin" })
    @Produces(MediaType.TEXT_PLAIN)
    public String admin() {
        return "admin role, ";
    }
}
