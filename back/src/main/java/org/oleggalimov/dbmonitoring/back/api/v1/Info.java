package org.oleggalimov.dbmonitoring.back.api.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/v1")
public class Info {

    @GET
    @Path("/status")
    @Produces({"application/json"})
    public Response getStatus() {
        return Response.ok().entity("Service online").build();
    }
}