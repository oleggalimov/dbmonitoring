package org.oleggalimov.dbmonitoring.back.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.implementations.RestResponceBody;
import org.oleggalimov.dbmonitoring.back.enumerations.BodyItemKeys;
import org.oleggalimov.dbmonitoring.back.enumerations.EventTypes;
import org.oleggalimov.dbmonitoring.back.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListUsers {

    private final ResponseBuilder responseBuilder;
    private final IUserService userService;

    @Autowired
    public ListUsers(ResponseBuilder responseBuilder, IUserService service) {
        this.responseBuilder = responseBuilder;
        this.userService = service;
    }

    @GetMapping("/userslist")
    @LogEvent(eventType = EventTypes.GET_REQUEST, message = "Call service /userslist")

    public String getUsersList() throws JsonProcessingException {
        try {
            RestResponceBody body = new RestResponceBody();
            if (userService == null) {
                throw new Exception("User service not configured!");
            } else {

                body.setItem(BodyItemKeys.USERS.toString(), userService.findAll());
                return responseBuilder.buildRestResponse(true, body, null, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}
