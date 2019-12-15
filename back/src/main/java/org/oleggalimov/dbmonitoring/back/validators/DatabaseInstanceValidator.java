package org.oleggalimov.dbmonitoring.back.validators;

import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseUser;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
import org.oleggalimov.dbmonitoring.back.enumerations.Errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseInstanceValidator {
    public static List<Error> validate(DataBaseInstance instance) {
        if (instance == null) {
            return Collections.singletonList(Errors.DB_INSTANCE_VALIDATION_EMPTY_INSTANCE.getError());
        }
        List<Error> errors = new ArrayList<>();
        String id = instance.getId();
        String host = instance.getHost();
        String sid = instance.getSid();
        Integer port = instance.getPort();
        DataBaseUser user = instance.getUser();
        DatabaseInstanceType type = instance.getType();


        if (id == null || StringValidator.isEmpty(id)) {
            errors.add(Errors.DB_INSTANCE_VALIDATION_EMPTY_ID.getError());
        }
        if (host == null || StringValidator.isEmpty(host)) {
            errors.add(Errors.DB_INSTANCE_VALIDATION_EMPTY_HOST.getError());
        }
        if (sid == null || StringValidator.isEmpty(sid)) {
            errors.add(Errors.DB_INSTANCE_VALIDATION_EMPTY_SID.getError());
        }
        if (port == null) {
            errors.add(Errors.DB_INSTANCE_VALIDATION_EMPTY_PORT.getError());
        }
        if (type == null) {
            errors.add(Errors.DB_INSTANCE_VALIDATION_EMPTY_TYPE.getError());
        }
        List<Error> userValidationErrors = DataBaseUserValidator.validate(user);
        if (!userValidationErrors.isEmpty()) {
            errors.addAll(userValidationErrors);
        }
        return errors;
    }
}
