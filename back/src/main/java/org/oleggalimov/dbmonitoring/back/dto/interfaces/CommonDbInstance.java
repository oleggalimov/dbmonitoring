package org.oleggalimov.dbmonitoring.back.dto.interfaces;

public interface CommonDbInstance {
    String getId();

    String getHost();

    int getPort();

    String getSid();

    CommonUser getUser();
}
