package org.oleggalimov.dbmonitoring.back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataBaseUser {
    private String login;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private transient String password;

    public DataBaseUser() {
    }

    public DataBaseUser(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void setLogin(String name) {
        this.login = name;
    }

    public String getLogin() {
        return this.login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
