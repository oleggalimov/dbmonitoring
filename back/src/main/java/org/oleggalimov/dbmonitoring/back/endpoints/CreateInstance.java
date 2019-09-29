package org.oleggalimov.dbmonitoring.back.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.implementations.DbInstanceImpl;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.validators.implementations.DbInstanceValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class CreateInstance {
    private final CopyOnWriteArraySet<CommonDbInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public CreateInstance(CopyOnWriteArraySet<CommonDbInstance> instanceSet, ResponseBuilder builder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = builder;
    }

    @PostMapping(value = "/createinstance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createInstance(@RequestBody(required = true) DbInstanceImpl instance) throws JsonProcessingException {
        try {
            List<Serializable> validationErrors = new DbInstanceValidatorImpl().validate(instance);
            if (validationErrors.size() > 0) {
                return responseBuilder.buildErrorResponse(validationErrors);
            }
            boolean added = instanceSet.add(instance);
            if (!added) {
                throw new Exception("Instance not added");
            }
            return responseBuilder.buildRestResponse(true, null, null, Collections.singletonList(Messages.DBINSTANCE_CREATED.getMessageObject()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }

    }
}
