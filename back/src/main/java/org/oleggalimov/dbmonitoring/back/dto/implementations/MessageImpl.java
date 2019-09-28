package org.oleggalimov.dbmonitoring.back.dto.implementations;

import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonMessage;
import org.oleggalimov.dbmonitoring.back.enumerations.MessageType;

import java.io.Serializable;

public class MessageImpl implements CommonMessage, Serializable {
    private String code;
    private String title;
    private String message;
    private MessageType type;

    public MessageImpl() {
    }

    public MessageImpl(String code, String title, String message, MessageType type) {
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

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String geTitle() {
        return title;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getType() {
        return type.name();
    }
}
