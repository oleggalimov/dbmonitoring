package org.oleggalimov.dbmonitoring.back.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.AuthRequest;
import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.enumerations.UserStatus;
import org.oleggalimov.dbmonitoring.back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@RestController


public class Login {
    private final ResponseBuilder responseBuilder;
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public Login(ResponseBuilder responseBuilder, UserService userService, BCryptPasswordEncoder encoder) {
        this.responseBuilder = responseBuilder;
        this.userService = userService;
        this.passwordEncoder = encoder;
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request, HttpServletResponse response) throws JsonProcessingException {
            try {
                if (request.getLogin().isEmpty() || request.getPassword().isEmpty()) {
                    return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.AUTH_EMPTY_DATA.getMessageObject()));
                }
                MonitoringSystemUser userDetails = userService.findUserByLogin(request.getLogin());
                if (userDetails==null) {
                    return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.AUTH_LOGIN_ERROR.getMessageObject()));
                } else if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                    return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.AUTH_LOGIN_ERROR.getMessageObject()));
                } else if (userDetails.getStatus()== UserStatus.BLOCKED) {
                    return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.AUTH_LOGIN_ERROR.getMessageObject()));
                } else {
                    return responseBuilder.buildRestResponse(true, null, null, null);
                }
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
                return responseBuilder.buildExceptionResponse(ex);
            }
    }
}
