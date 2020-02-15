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
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class CreateUser {
    private final ResponseBuilder responseBuilder;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public CreateUser(ResponseBuilder responseBuilder, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.responseBuilder = responseBuilder;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired

    @Secured(value = {"ROLE_USER_ADMIN"})
    @CrossOrigin(value = {"http://localhost:9000"})
    @PostMapping(value = "create/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @LogHttpEvent(eventType = RequestMethod.POST, message = "create/user")
    public String createUser(@RequestBody MonitoringSystemUser monitoringSystemUser) throws JsonProcessingException {
        try {
            List<Error> validationErrors = UserValidator.validate(monitoringSystemUser);
            if (validationErrors.size() != 0) {
                return responseBuilder.buildRestResponse(false, null, validationErrors, null);
            }
            String encryptedPassword = passwordEncoder.encode(monitoringSystemUser.getPassword());
            monitoringSystemUser.setPassword(encryptedPassword);
            MonitoringSystemUser added = userService.saveUser(monitoringSystemUser);
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
