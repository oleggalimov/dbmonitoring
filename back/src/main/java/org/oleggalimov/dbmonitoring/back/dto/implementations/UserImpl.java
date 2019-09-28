package org.oleggalimov.dbmonitoring.back.dto.implementations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonUser;

import java.io.Serializable;

public class UserImpl implements CommonUser, Serializable {
    private String name;
    @JsonIgnore
    private transient String password;

    public UserImpl() {
    }

    public UserImpl(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
