package org.oleggalimov.dbmonitoring.back.validators.interfaces;

import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;

import java.io.Serializable;
import java.util.List;

public interface DBInstanceValidator {
    List<Serializable> validate(CommonDbInstance instance);
}
