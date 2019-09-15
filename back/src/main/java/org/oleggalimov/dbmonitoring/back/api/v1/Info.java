package org.oleggalimov.dbmonitoring.back.api.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

@Path("/v1")
public class Info extends Application {

    @GET
    @Path("/status")
    @Produces({"application/json; charset=UTF-8"})
    public Response getStatus() {
        System.out.println("Запрос на сервлет");
        return Response.ok().entity("Service online").build();
    }
}