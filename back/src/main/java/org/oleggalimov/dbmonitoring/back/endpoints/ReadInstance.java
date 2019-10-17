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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.oleggalimov.dbmonitoring.back.enumerations.Messages.DBINSTANCE_IS_ABSENT;

@RestController
public class ReadInstance {
    private final CopyOnWriteArraySet<CommonDbInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public ReadInstance(CopyOnWriteArraySet<CommonDbInstance> instanceSet, ResponseBuilder builder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = builder;
    }

    @LogEvent(eventType = EventTypes.GET_REQUEST, message = "Вызов readinstance/{id}")
    @GetMapping("readinstance/{id}")
    public String getInstance(@PathVariable String id) throws JsonProcessingException {
        try {
            if (id == null || id.equals("")) {
                throw new Exception("Id is incorrect or empty.");
            } else if (instanceSet == null) {
                throw new Exception("Instances not configured!");
            }
            for (CommonDbInstance instance : instanceSet) {
                if (instance.getId().equals(id)) {
                    RestResponceBody body = new RestResponceBody();
                    body.setItem(BodyItemKeys.INSTANCES.toString(), Collections.singletonList(instance));
                    return responseBuilder.buildRestResponse(true, body, null, null);
                }
            }
            return responseBuilder.buildErrorResponse(Collections.singletonList(DBINSTANCE_IS_ABSENT.getMessageObject()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}