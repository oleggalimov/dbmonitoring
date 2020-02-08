package org.oleggalimov.dbmonitoring.back.endpoints.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.enumerations.Role;
import org.oleggalimov.dbmonitoring.back.services.UserService;
import org.oleggalimov.dbmonitoring.back.validators.StringValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin(origins = "*")
    @DeleteMapping("delete/user/{login}")
    @LogHttpEvent(eventType = RequestMethod.DELETE, message = "delete/user/{login}")
    @Secured(value = {"USER_ADMIN"})
    public String deleteUser(@PathVariable String login) throws JsonProcessingException {
        try {
            if (StringValidator.isEmpty(login)) {
                throw new Exception("Login is empty.");
            }
            MonitoringSystemUser monitoringSystemUser = userService.findUserByLogin(login);
            if (monitoringSystemUser == null) {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.USER_IS_ABSENT.getMessageObject()));
            } else {
                long deleted = userService.deleteUser(monitoringSystemUser);
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