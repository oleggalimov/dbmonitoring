package org.oleggalimov.dbmonitoring.back.endpoints.instance;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.enumerations.Errors;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.validators.StringValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class DeleteInstance {
    private final CopyOnWriteArraySet<DataBaseInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public DeleteInstance(CopyOnWriteArraySet<DataBaseInstance> instanceSet, ResponseBuilder builder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = builder;
    }

    @Secured(value = {"ROLE_ADMIN"})
    @DeleteMapping("delete/instance/{id}")
    @LogHttpEvent(eventType = RequestMethod.DELETE, message = " delete/instance/{id}")
    public String deleteInstance(@PathVariable String id) throws JsonProcessingException {
        try {
            if (StringValidator.isEmpty(id)) {
                return responseBuilder.buildRestResponse(false, null, Collections.singletonList(Errors.DB_INSTANCE_VALIDATION_EMPTY_ID.getError()), null);
            }
            boolean wasRemoved = false;
            for (DataBaseInstance instance : instanceSet) {
                if (instance.getId().equals(id)) {
                    wasRemoved = instanceSet.remove(instance);
                    break;
                }
            }
            if (!wasRemoved) {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.DB_INSTANCE_IS_NOT_DELETED.getMessageObject()));
            } else {
                return responseBuilder.buildRestResponse(true, null, null, Collections.singletonList(Messages.DB_INSTANCE_DELETED.getMessageObject()));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}