package org.oleggalimov.dbmonitoring.back.validators.interfaces;

import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonUser;

import java.io.Serializable;
import java.util.List;

public interface UserValidator {
    List<Serializable> validate(CommonUser user);
}
