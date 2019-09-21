package rest;

/*
  Based on https://www.baeldung.com/jersey-test
 */

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.oleggalimov.dbmonitoring.back.api.v1.Info;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StatusTest extends JerseyTest {
      @Override
    protected Application configure() {
          return new ResourceConfig(Info.class);
    }

    @Test
    public void testV1StatusRecource() {
        Response response = target("/v1/status")
                .request()
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals( MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));

        String content = response.readEntity(String.class);
        assertEquals("Service online", content);
    }
}