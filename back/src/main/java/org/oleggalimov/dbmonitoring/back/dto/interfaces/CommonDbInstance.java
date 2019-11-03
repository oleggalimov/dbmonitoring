package org.oleggalimov.dbmonitoring.back.dto.interfaces;

public interface CommonDbInstance {
    String getId();

    String getHost();

    Integer getPort();

    String getSid();

    CommonDbUser getUser();

    boolean deepEquals(CommonDbInstance otherInstance);

}
