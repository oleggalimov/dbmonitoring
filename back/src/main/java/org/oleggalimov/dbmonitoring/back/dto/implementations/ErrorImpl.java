package org.oleggalimov.dbmonitoring.back.dto.implementations;

import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonError;

import java.io.Serializable;

public class ErrorImpl implements CommonError, Serializable {
    private String code;
    private String title;
    private String message;

    public ErrorImpl(String code, String title, String message) {
        this.code = code;
        this.title = title;
        this.message = message;
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


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
