package org.oleggalimov.dbmonitoring.back.dto.implementations;

import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonUser;

import java.io.Serializable;

public class DbInstanceImpl implements CommonDbInstance, Serializable {

    private String id;
    private String host;
    private int port;
    private String sid;
    private UserImpl user;

    public DbInstanceImpl() {
    }

    public DbInstanceImpl(String id, String host, int port, String sid, UserImpl user) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.sid = sid;
        this.user = user;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setUser(UserImpl user) {
        this.user = user;
    }

    @Override
    public CommonUser getUser() {
        return user;
    }
}
