package org.oleggalimov.dbmonitoring.back.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.oleggalimov.dbmonitoring.back.CommonResponceBuilder;
import org.oleggalimov.dbmonitoring.back.dto.implementations.ErrorImpl;
import org.oleggalimov.dbmonitoring.back.dto.implementations.RestResponceBody;
import org.oleggalimov.dbmonitoring.back.dto.implementations.RestResponse;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonBody;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResponceBuilder implements CommonResponceBuilder {
    private ObjectMapper mapper;

    public ResponceBuilder() {
    }

    @Autowired
    public ResponceBuilder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String buildRestResponce(boolean success, CommonBody body, List<Serializable> errors, List<Serializable> messages) throws JsonProcessingException {
        CommonBody responseBody = body == null ? new RestResponceBody() : body;
        List<Serializable> responceErrors = errors == null ? new ArrayList<>() : errors;
        List<Serializable> responceMessages = errors == null ? new ArrayList<>() : messages;
        RestResponse response = new RestResponse(success, responseBody, responceErrors, responceMessages);
        return mapper.writeValueAsString(response);
    }

    public String buildExceptionResponce(Throwable ex) throws JsonProcessingException {
        ErrorImpl error = new ErrorImpl("01", "Service error", ex.getCause().toString());
        RestResponse response = new RestResponse(false, null, Collections.singletonList(error), Collections.emptyList());
        return mapper.writeValueAsString(response);
    }

}
