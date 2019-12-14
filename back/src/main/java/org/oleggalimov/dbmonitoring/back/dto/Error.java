package org.oleggalimov.dbmonitoring.back.dto;

public class Error {
    private String code;
    private String title;
    private String message;

    public Error() {
    }

    public Error(String code, String title, String message) {
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

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Error{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
