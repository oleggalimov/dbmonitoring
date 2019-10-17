package org.oleggalimov.dbmonitoring.back.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.implementations.RestResponceBody;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.oleggalimov.dbmonitoring.back.enumerations.BodyItemKeys;
import org.oleggalimov.dbmonitoring.back.enumerations.EventTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class ListInstances {
    private final CopyOnWriteArraySet<CommonDbInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public ListInstances(CopyOnWriteArraySet<CommonDbInstance> instanceSet, ResponseBuilder builder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = builder;
    }

    @GetMapping("/list")
    @LogEvent(eventType = EventTypes.GET_REQUEST, message = "Call service /list")
    public String getInstances() throws JsonProcessingException {
        try {
            RestResponceBody body = new RestResponceBody();
            if (instanceSet == null) {
                throw new Exception("Instances not configured!");
            } else {
                body.setItem(BodyItemKeys.INSTANCES.toString(), instanceSet);
                return responseBuilder.buildRestResponse(true, body, null, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}
