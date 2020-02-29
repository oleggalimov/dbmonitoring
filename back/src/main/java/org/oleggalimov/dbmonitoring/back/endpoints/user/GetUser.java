package org.oleggalimov.dbmonitoring.back.endpoints.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.RestResponseBody;
import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.oleggalimov.dbmonitoring.back.enumerations.BodyItemKey;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.services.UserService;
import org.oleggalimov.dbmonitoring.back.validators.StringValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class GetUser {
    private final ResponseBuilder responseBuilder;
    private final UserService userService;

    @Autowired
    public GetUser(ResponseBuilder responseBuilder, UserService service) {
        this.responseBuilder = responseBuilder;
        this.userService = service;
    }

    @Secured(value = {"ROLE_USER_ADMIN"})
    @GetMapping("list/user/{login}")
    @LogHttpEvent(eventType = RequestMethod.GET, message = " list/user/{login}")
    public String getUser(@PathVariable String login) throws JsonProcessingException {
        try {
            if (StringValidator.isEmpty(login)) {
                throw new Exception("Login is empty.");
            }
            MonitoringSystemUser monitoringSystemUser = userService.findUserByLogin(login);
            if (monitoringSystemUser == null) {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.USER_IS_ABSENT.getMessageObject()));
            } else {
                RestResponseBody body = new RestResponseBody();
                body.setItem(BodyItemKey.USERS.toString(), Collections.singletonList(monitoringSystemUser));
                return responseBuilder.buildRestResponse(true, body, null, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}