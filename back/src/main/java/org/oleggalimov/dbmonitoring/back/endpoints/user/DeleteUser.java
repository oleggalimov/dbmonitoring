package org.oleggalimov.dbmonitoring.back.endpoints.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.entities.User;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.services.UserService;
import org.oleggalimov.dbmonitoring.back.validators.StringValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class DeleteUser {
    private final UserService userService;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public DeleteUser(ResponseBuilder responseBuilder, UserService service) {
        this.responseBuilder = responseBuilder;
        this.userService = service;
    }

    @DeleteMapping("delete/user/{login}")
    @LogHttpEvent(eventType = RequestMethod.DELETE, message = "delete/user/{login}")
    public String deleteUser(@PathVariable String login) throws JsonProcessingException {
        try {
            if (StringValidator.isEmpty(login)) {
                throw new Exception("Login is empty.");
            }
            User user = userService.findUserByLogin(login);
            if (user == null) {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.USER_IS_ABSENT.getMessageObject()));
            } else {
                long deleted = userService.deleteUser(user);
                if (deleted == 0) {
                    return responseBuilder.buildRestResponse(true, null, null, Collections.singletonList(Messages.USER_DELETED.getMessageObject()));
                } else {
                    return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.USER_IS_NOT_DELETED.getMessageObject()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}