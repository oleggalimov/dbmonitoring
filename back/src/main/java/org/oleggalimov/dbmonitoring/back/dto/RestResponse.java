package org.oleggalimov.dbmonitoring.back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse {
    private boolean success;
    private RestResponseBody body;
    private List<Error> errors;
    private List<Message> messages;

    public RestResponse() {
    }

    public RestResponse(boolean success, RestResponseBody body, List<Error> errors, List<Message> messages) {
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

    public RestResponseBody getBody() {
        return body;
    }

    public void setBody(RestResponseBody body) {
        this.body = body;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
