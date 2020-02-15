package org.oleggalimov.dbmonitoring.back.endpoints.instance;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.validators.DatabaseInstanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.oleggalimov.dbmonitoring.back.enumerations.Messages.DB_INSTANCE_IS_ABSENT;

@RestController
public class UpdateInstance {
    private final CopyOnWriteArraySet<DataBaseInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public UpdateInstance(CopyOnWriteArraySet<DataBaseInstance> instanceSet, ResponseBuilder builder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = builder;
    }
    @Secured(value = {"ROLE_ADMIN"})
    @CrossOrigin(value = {"http://localhost:9000"})
    @PutMapping(value = "update/instance")
    @LogHttpEvent(eventType = RequestMethod.PUT, message = "update/instance")
    public String updateInstance(@RequestBody DataBaseInstance requestInstance) throws JsonProcessingException {
        try {
            List<Error> validationErrors = DatabaseInstanceValidator.validate(requestInstance);
            if (validationErrors.size() > 0) {
                return responseBuilder.buildRestResponse(false, null, validationErrors, null);
            } else if (!instanceSet.contains(requestInstance)) {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(DB_INSTANCE_IS_ABSENT.getMessageObject()));
            }
            instanceSet.remove(requestInstance);
            boolean add = instanceSet.add(requestInstance);
            if (!add) {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.DB_INSTANCE_IS_NOT_UPDATED.getMessageObject()));
            } else {
                return responseBuilder.buildRestResponse(true, null, null, Collections.singletonList(Messages.DB_INSTANCE_UPDATED.getMessageObject()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}
