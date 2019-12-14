package org.oleggalimov.dbmonitoring.back.endpoints.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.entities.User;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.services.UserService;
import org.oleggalimov.dbmonitoring.back.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class CreateUser {
    private final ResponseBuilder responseBuilder;
    private final UserService userService;

    @Autowired
    public CreateUser(ResponseBuilder responseBuilder, UserService service) {
        this.responseBuilder = responseBuilder;
        this.userService = service;
    }

    @PostMapping(value = "create/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @LogHttpEvent(eventType = RequestMethod.POST, message = "create/user")
    public String createUser(@RequestBody User user) throws JsonProcessingException {
        try {
            List<Error> validationErrors = UserValidator.validate(user);
            if (validationErrors.size() != 0) {
                return responseBuilder.buildRestResponse(false, null, validationErrors, null);
            }
            User added = userService.saveUser(user);
            if (added == null) {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.USER_IS_NOT_ADDED.getMessageObject()));
            } else {
                return responseBuilder.buildRestResponse(true, null, null, Collections.singletonList(Messages.USER_CREATED.getMessageObject()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }

    }
}
