package org.oleggalimov.dbmonitoring.back.dto.implementations;

import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbUser;

import java.io.Serializable;

public class DbInstanceImpl implements CommonDbInstance, Serializable {

    private String id;
    private String host;
    private Integer port;
    private String sid;
    private DbUserImpl user;

    public DbInstanceImpl() {
    }

    public DbInstanceImpl(String id, String host, Integer port, String sid, DbUserImpl user) {
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
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setUser(DbUserImpl user) {
        this.user = user;
    }

    @Override
    public CommonDbUser getUser() {
        return user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof CommonDbInstance)) {
            return false;
        } else if (this.id == null) {
            return false;
        } else {
            CommonDbInstance otherInstance = (CommonDbInstance) obj;
            String otherInstanceId = otherInstance.getId();
            return this.id.equals(otherInstanceId);
        }

    }

    @Override
    public boolean deepEquals(CommonDbInstance otherInstance) {
        if (!this.id.equals(otherInstance.getId())) {
            return false;
        } else if (!this.host.equals(otherInstance.getHost())) {
            return false;
        } else if (!this.port.equals(otherInstance.getPort())) {
            return false;
        } else if (!this.sid.equals(otherInstance.getSid())) {
            return false;
        } else return this.user.equals(otherInstance.getUser());
    }
}
