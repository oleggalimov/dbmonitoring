package org.oleggalimov.dbmonitoring.back.endpoints.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.services.UserService;
import org.oleggalimov.dbmonitoring.back.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class UpdateUser {
    private final UserService userService;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public UpdateUser(UserService userService, ResponseBuilder builder) {
        this.userService = userService;
        this.responseBuilder = builder;
    }
    @CrossOrigin(origins = "*")
    @PutMapping(value = "update/user")
    @LogHttpEvent(eventType = RequestMethod.PUT, message = "update/user")
    public String updateUser(@RequestBody MonitoringSystemUser monitoringSystemUser) throws JsonProcessingException {
        try {
            List<Error> validationErrors = UserValidator.validate(monitoringSystemUser);
            if (validationErrors.size() > 0) {
                return responseBuilder.buildRestResponse(false, null, validationErrors, null);
            }
            boolean update = userService.updateUser(monitoringSystemUser);
            if (update) {
                return responseBuilder.buildRestResponse(true, null, null, Collections.singletonList(Messages.USER_UPDATED.getMessageObject()));
            } else {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.USER_IS_NOT_UPDATED.getMessageObject()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}
