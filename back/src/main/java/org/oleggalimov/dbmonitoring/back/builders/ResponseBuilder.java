package org.oleggalimov.dbmonitoring.back.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.dto.Message;
import org.oleggalimov.dbmonitoring.back.dto.RestResponse;
import org.oleggalimov.dbmonitoring.back.dto.RestResponseBody;
import org.oleggalimov.dbmonitoring.back.enumerations.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ResponseBuilder implements CommonResponseBuilder {
    private ObjectMapper mapper;

    public ResponseBuilder() {
    }

    @Autowired
    public ResponseBuilder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String buildRestResponse(boolean success, RestResponseBody body, List<Error> errors, List<Message> messages) throws JsonProcessingException {
        RestResponseBody responseBody = body == null ? new RestResponseBody() : body;
        List<Error> responseErrors = errors == null ? new ArrayList<>() : errors;
        List<Message> responseMessages = messages == null ? new ArrayList<>() : messages;
        RestResponse response = new RestResponse(success, responseBody, responseErrors, responseMessages);
        return mapper.writeValueAsString(response);
    }

    public String buildExceptionResponse(Throwable ex) throws JsonProcessingException {
        String msg;
        msg = String.valueOf(ex.getCause());
        if (msg.equals("null")) {
            msg = String.valueOf(ex.getMessage());
        }
        List<Error> errorList = new ArrayList<>();
        if (msg.length() > 255) {
            errorList.add(new Error(ErrorCode.REST_EXCEPTION.name(), "Critical error", "Full message available in log files"));
        } else {
            errorList.add(new Error(ErrorCode.REST_EXCEPTION.name(), "Critical error", msg));
        }
        return buildRestResponse(false, null, errorList, null);
    }
}
