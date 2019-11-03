package org.oleggalimov.dbmonitoring.back.validators.interfaces;

import org.oleggalimov.dbmonitoring.back.dto.CommonUser;

import java.io.Serializable;
import java.util.List;

public interface UserValidator<T extends CommonUser> {
    List<Serializable> validate(T user);
}
