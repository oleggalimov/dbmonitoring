package org.oleggalimov.dbmonitoring.back.dto;

import org.oleggalimov.dbmonitoring.back.enumerations.Roles;

import java.util.Set;

public abstract class CommonUser {
    private String login;
    private String email;

    CommonUser() {
    }

    CommonUser(String login, String eMail, Set<Roles> roles) {
        this.login = login;
        this.email = eMail;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
