package org.oleggalimov.dbmonitoring.back.endpoints.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.RestResponseBody;
import org.oleggalimov.dbmonitoring.back.enumerations.BodyItemKey;
import org.oleggalimov.dbmonitoring.back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListUsers {

    private final ResponseBuilder responseBuilder;
    private final UserService userService;

    @Autowired
    public ListUsers(ResponseBuilder responseBuilder, UserService service) {
        this.responseBuilder = responseBuilder;
        this.userService = service;
    }

    @GetMapping("list/user")
    @LogHttpEvent(eventType = RequestMethod.GET, message = "list/user")

    public String listUsers() throws JsonProcessingException {
        try {
            RestResponseBody body = new RestResponseBody();
            body.setItem(BodyItemKey.USERS.toString(), userService.findAll());
            return responseBuilder.buildRestResponse(true, body, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}
