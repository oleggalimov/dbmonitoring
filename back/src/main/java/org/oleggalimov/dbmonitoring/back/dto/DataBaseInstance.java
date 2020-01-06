package org.oleggalimov.dbmonitoring.back.dto;

import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;

public class DataBaseInstance {

    private String id;
    private String host;
    private Integer port;
    private String database;
    private DataBaseUser user;
    private DatabaseInstanceType type;

    public DataBaseInstance() {
    }

    public DataBaseInstance(String id, String host, Integer port, String database, DataBaseUser user, DatabaseInstanceType type) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    public void setUser(DataBaseUser user) {
        this.user = user;
    }

    public DataBaseUser getUser() {
        return user;
    }

    public DatabaseInstanceType getType() {
        return type;
    }

    public void setType(DatabaseInstanceType type) {
        this.type = type;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof DataBaseInstance)) {
            return false;
        } else if (this.id == null) {
            return false;
        } else {
            DataBaseInstance otherInstance = (DataBaseInstance) obj;
            String otherInstanceId = otherInstance.getId();
            return this.id.equals(otherInstanceId) && (this.type == otherInstance.type);
        }

    }

    public boolean deepEquals(DataBaseInstance otherInstance) {
        if (!this.id.equals(otherInstance.getId())) {
            return false;
        } else if (!this.host.equalsIgnoreCase(otherInstance.getHost())) {
            return false;
        } else if (!this.port.equals(otherInstance.getPort())) {
            return false;
        } else if (this.type != otherInstance.type) {
            return false;
        } else if (!this.database.equalsIgnoreCase(otherInstance.getDatabase())) {
            return false;
        } else return this.user.equals(otherInstance.getUser());
    }

    @Override
    public String toString() {
        return "DataBaseInstance{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", database='" + database + '\'' +
                ", user=" + user +
                ", type=" + type +
                '}';
    }
}
