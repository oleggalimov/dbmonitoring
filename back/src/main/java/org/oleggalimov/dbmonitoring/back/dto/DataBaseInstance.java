package org.oleggalimov.dbmonitoring.back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;

@Data
@NoArgsConstructor
public class DataBaseInstance implements Cloneable {

    private String id;
    private String host;
    private Integer port;
    private String database;
    private DataBaseUser user;
    private DatabaseInstanceType type;
    private String status;


    public DataBaseInstance(String id, String host, Integer port, String database, DataBaseUser user, DatabaseInstanceType type) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.type = type;
    }

    public boolean equals(Object obj) {
        if (obj==null) {
            return false;
        } else if (!(obj instanceof DataBaseInstance)) {
            return false;
        } else if (this.id==null) {
            return false;
        } else {
            DataBaseInstance otherInstance = (DataBaseInstance) obj;
            String otherInstanceId = otherInstance.getId();
            return this.id.equals(otherInstanceId);
        }

    }

    @Override
    public DataBaseInstance clone() {
        try {
            return (DataBaseInstance) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return new DataBaseInstance(id, host, port, database, user, type);
        }
    }
}
