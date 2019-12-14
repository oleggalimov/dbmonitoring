package org.oleggalimov.dbmonitoring.back.endpoints.instance;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.dto.RestResponseBody;
import org.oleggalimov.dbmonitoring.back.enumerations.BodyItemKey;
import org.oleggalimov.dbmonitoring.back.enumerations.Errors;
import org.oleggalimov.dbmonitoring.back.validators.StringValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.oleggalimov.dbmonitoring.back.enumerations.Messages.DB_INSTANCE_IS_ABSENT;

@RestController
public class GetInstance {
    private final CopyOnWriteArraySet<DataBaseInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public GetInstance(CopyOnWriteArraySet<DataBaseInstance> instanceSet, ResponseBuilder builder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = builder;
    }


    @GetMapping("list/instance/{id}")
    @LogHttpEvent(eventType = RequestMethod.GET, message = " list/instance/{id}")
    public String getInstance(@PathVariable String id) throws JsonProcessingException {
        try {
            if (id == null || StringValidator.isEmpty(id)) {
                return responseBuilder.buildRestResponse(false, null, Collections.singletonList(Errors.DB_INSTANCE_VALIDATION_EMPTY_ID.getError()), null);
            }
            for (DataBaseInstance instance : instanceSet) {
                if (instance.getId().equals(id)) {
                    RestResponseBody body = new RestResponseBody();
                    body.setItem(BodyItemKey.INSTANCES.toString(), Collections.singletonList(instance));
                    return responseBuilder.buildRestResponse(true, body, null, null);
                }
            }
            return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(DB_INSTANCE_IS_ABSENT.getMessageObject()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}