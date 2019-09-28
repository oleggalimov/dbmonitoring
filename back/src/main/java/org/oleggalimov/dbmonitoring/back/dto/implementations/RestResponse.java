package org.oleggalimov.dbmonitoring.back.dto.implementations;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonBody;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonResponse;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse implements CommonResponse, Serializable {
    private boolean success;
    private CommonBody body;
    private List<Serializable> errors;
    private List<Serializable> messages;

    public RestResponse() {
    }

    public RestResponse(boolean success, CommonBody body, List<Serializable> errors, List<Serializable> messages) {
        this.success = success;
        this.body = body;
        this.errors = errors;
        this.messages = messages;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public CommonBody getBody() {
        return body;
    }

    public void setBody(CommonBody body) {
        this.body = body;
    }

    @Override
    public List<Serializable> getErrors() {
        return errors;
    }

    public void setErrors(List<Serializable> errors) {
        this.errors = errors;
    }

    @Override
    public List<Serializable> getMessages() {
        return messages;
    }

    public void setMessages(List<Serializable> messages) {
        this.messages = messages;
    }
}
