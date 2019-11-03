package org.oleggalimov.dbmonitoring.back.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.oleggalimov.dbmonitoring.back.dto.implementations.ErrorImpl;
import org.oleggalimov.dbmonitoring.back.dto.implementations.RestResponse;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonBody;
import org.oleggalimov.dbmonitoring.back.enumerations.ErrorsCode;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResponseBuilder implements CommonResponseBuilder {
    private ObjectMapper mapper;

    public ResponseBuilder() {
    }

    @Autowired
    public ResponseBuilder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String buildRestResponse(boolean success, CommonBody body, List<Serializable> errors, List<Serializable> messages) throws JsonProcessingException {
        List<Serializable> responseErrors = errors == null ? new ArrayList<>() : errors;
        List<Serializable> responseMessages = messages == null ? new ArrayList<>() : messages;
        RestResponse response = new RestResponse(success, body, responseErrors, responseMessages);
        return mapper.writeValueAsString(response);
    }

    public String buildExceptionResponse(Throwable ex) throws JsonProcessingException {
        String msg;
        msg = String.valueOf(ex.getCause());
        if (msg.equals("null")) {
            msg = String.valueOf(ex.getMessage());
        }
        ErrorImpl error;
        if (msg.length() > 255) {
            error = new ErrorImpl(ErrorsCode.REST_EXCEPTION.name(), "Critical error", "Full message available in log files");
        } else {
            error = new ErrorImpl(ErrorsCode.REST_EXCEPTION.name(), "Critical error", msg);
        }
        RestResponse response = new RestResponse(false, null, Collections.singletonList(error), Collections.singletonList(Messages.SERVICE_EXCEPTION.getMessageObject()));
        return mapper.writeValueAsString(response);
    }

    public String buildErrorResponse(List<Serializable> messages) throws JsonProcessingException {
        RestResponse response = new RestResponse(false, null, Collections.emptyList(), messages);
        return mapper.writeValueAsString(response);
    }


}
