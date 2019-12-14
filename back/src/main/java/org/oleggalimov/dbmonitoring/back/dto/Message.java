package org.oleggalimov.dbmonitoring.back.dto;

import org.oleggalimov.dbmonitoring.back.enumerations.MessageType;

public class Message {
    private String code;
    private String title;
    private String message;
    private MessageType type;

    public Message() {
    }

    public Message(String code, String title, String message, MessageType type) {
        this.code = code;
        this.title = title;
        this.message = message;
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getType() {
        return type;
    }
}
