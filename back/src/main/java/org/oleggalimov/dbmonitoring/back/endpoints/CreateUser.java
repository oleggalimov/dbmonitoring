package org.oleggalimov.dbmonitoring.back.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.CommonUser;
import org.oleggalimov.dbmonitoring.back.dto.User;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.services.interfaces.IUserService;
import org.oleggalimov.dbmonitoring.back.validators.implementations.UserValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@RestController
public class CreateUser {
    private final ResponseBuilder responseBuilder;
    private final IUserService userService;

    @Autowired
    public CreateUser(ResponseBuilder responseBuilder, IUserService service) {
        this.responseBuilder = responseBuilder;
        this.userService = service;
    }

    @PostMapping(value = "/createuser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createInstance(@RequestBody(required = true) User user) throws JsonProcessingException {
        try {
            if (user == null) {
                throw new Exception("Instances not configured!");
            }
            List<Serializable> validationErrors = new UserValidatorImpl().validate(user);
            if (validationErrors.size() > 0) {
                return responseBuilder.buildErrorResponse(validationErrors);
            }
            CommonUser added = userService.saveUser(user);
            if (added == null) {
                throw new Exception("Instance not added");
            }
            return responseBuilder.buildRestResponse(true, null, null, Collections.singletonList(Messages.DBINSTANCE_CREATED.getMessageObject()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }

    }
}
