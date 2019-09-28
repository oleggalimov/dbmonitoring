package org.oleggalimov.dbmonitoring.back.dto.interfaces;

import java.io.Serializable;
import java.util.List;

public interface CommonResponse {
    boolean isSuccess();

    CommonBody getBody();

    List<Serializable> getErrors();

    List<Serializable> getMessages();
}
