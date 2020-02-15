package org.oleggalimov.dbmonitoring.back.endpoints.instance;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.validators.DatabaseInstanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class CreateInstance {
    private final CopyOnWriteArraySet<DataBaseInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public CreateInstance(CopyOnWriteArraySet<DataBaseInstance> instanceSet, ResponseBuilder builder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = builder;
    }
    @Secured(value = {"ROLE_ADMIN"})
    @CrossOrigin(value = {"http://localhost:9000"})
    @PostMapping(value = "create/instance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @LogHttpEvent(eventType = RequestMethod.POST, message = " create/instance")
    public String createInstance(@RequestBody DataBaseInstance instance) throws JsonProcessingException {
        try {
            List<Error> validationErrors = DatabaseInstanceValidator.validate(instance);
            if (validationErrors.size() > 0) {
                return responseBuilder.buildRestResponse(false, null, validationErrors, null);
            }
            DataBaseInstance existedInstance = null;
            boolean idAlreadyExists = instanceSet.stream().anyMatch(item -> item.getId().equalsIgnoreCase(instance.getId()));
            if (idAlreadyExists) {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.DB_INSTANCE_IS_ALREADY_EXIST.getMessageObject()));
            }
            boolean added = instanceSet.add(instance);
            if (!added) {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.DB_INSTANCE_IS_NOT_ADDED.getMessageObject()));
            } else {
                return responseBuilder.buildRestResponse(true, null, null, Collections.singletonList(Messages.DB_INSTANCE_CREATED.getMessageObject()));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }

    }
}
