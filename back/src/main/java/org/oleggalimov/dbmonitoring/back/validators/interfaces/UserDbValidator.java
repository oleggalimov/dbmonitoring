package org.oleggalimov.dbmonitoring.back.validators.interfaces;

import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbUser;

import java.io.Serializable;
import java.util.List;

public interface UserDbValidator {
    List<Serializable> validate(CommonDbUser user);
}
