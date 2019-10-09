package org.oleggalimov.dbmonitoring.back.dto.interfaces;

public interface CommonDbInstance {
    String getId();

    String getHost();

    Integer getPort();

    String getSid();

    CommonUser getUser();

    boolean deepEquals(CommonDbInstance otherInstance);

}
