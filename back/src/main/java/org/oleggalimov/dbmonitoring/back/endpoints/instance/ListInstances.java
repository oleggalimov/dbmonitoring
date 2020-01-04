package org.oleggalimov.dbmonitoring.back.endpoints.instance;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.dto.RestResponseBody;
import org.oleggalimov.dbmonitoring.back.enumerations.BodyItemKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class ListInstances {
    private final CopyOnWriteArraySet<DataBaseInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public ListInstances(CopyOnWriteArraySet<DataBaseInstance> instanceSet, ResponseBuilder builder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = builder;
    }

    @GetMapping("list/instance/all")
    @LogHttpEvent(eventType = RequestMethod.GET, message = "list/instance/all")
    public String listInstances() throws JsonProcessingException {
        try {
            RestResponseBody body = new RestResponseBody();
            body.setItem(BodyItemKey.INSTANCES.toString(), instanceSet);
            return responseBuilder.buildRestResponse(true, body, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}
