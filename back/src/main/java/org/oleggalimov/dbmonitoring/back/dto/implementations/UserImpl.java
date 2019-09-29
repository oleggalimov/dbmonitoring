package org.oleggalimov.dbmonitoring.back.dto.implementations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonUser;

import java.io.Serializable;

public class UserImpl implements CommonUser, Serializable {
    private String login;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private transient String password;

    public UserImpl() {
    }

    public UserImpl(String login, String password) {
        this.login = login;
        this.password = password;
    }


    public void setLogin(String name) {
        this.login = name;
    }

    @Override
    public String getLogin() {
        return this.login;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
