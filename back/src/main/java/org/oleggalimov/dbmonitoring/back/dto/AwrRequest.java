package org.oleggalimov.dbmonitoring.back.dto;

public class AwrRequest {
    String instanceId;
    String beginInterval;
    String endInterval;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getBeginInterval() {
        return beginInterval;
    }

    public void setBeginInterval(String beginInterval) {
        this.beginInterval = beginInterval;
    }

    public String getEndInterval() {
        return endInterval;
    }

    public void setEndInterval(String endInterval) {
        this.endInterval = endInterval;
    }
}
