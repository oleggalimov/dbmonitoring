package org.oleggalimov.dbmonitoring.back.entities;

import java.sql.Timestamp;

public class AwrDataParams {
    Long dbId;
    Long instanceNumber;
    Long startId;
    Long endId;

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public Long getInstanceNumber() {
        return instanceNumber;
    }

    public void setInstanceNumber(Long instanceNumber) {
        this.instanceNumber = instanceNumber;
    }

    public Long getStartId() {
        return startId;
    }

    public void setStartId(Long startId) {
        this.startId = startId;
    }

    public Long getEndId() {
        return endId;
    }

    public void setEndId(Long endId) {
        this.endId = endId;
    }
}
