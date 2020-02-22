package org.oleggalimov.dbmonitoring.back.dto;

import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
import org.oleggalimov.dbmonitoring.back.enumerations.InfluxTimePeriod;

public class PointsRequest {
    private String instanceId;
    private DatabaseInstanceType databaseInstanceType;
    private String measurement;
    private InfluxTimePeriod timePeriod;

    
    public PointsRequest() {
    }

    public String getDataBaseId() {
        return instanceId;
    }

    public void setDataBaseId(String dataBaseId) {
        this.instanceId = dataBaseId;
    }

    public DatabaseInstanceType getDatabaseInstanceType() {
        return databaseInstanceType;
    }

    public void setDatabaseInstanceType(DatabaseInstanceType databaseInstanceType) {
        this.databaseInstanceType = databaseInstanceType;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public InfluxTimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(InfluxTimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }
}
