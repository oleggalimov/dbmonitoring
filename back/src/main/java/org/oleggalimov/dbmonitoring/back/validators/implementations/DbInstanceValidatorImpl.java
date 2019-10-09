package org.oleggalimov.dbmonitoring.back.validators.implementations;

import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonUser;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.validators.interfaces.DBInstanceValidator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DbInstanceValidatorImpl implements DBInstanceValidator {
    @Override
    public List<Serializable> validate(CommonDbInstance instance) {
        List<Serializable> errors = new ArrayList<>();
        String id = instance.getId();
        String host = instance.getHost();
        String sid = instance.getSid();
        Integer port = instance.getPort();
        CommonUser user = instance.getUser();

        if (id == null || host == null || sid == null || port == null) {
            errors.add(Messages.DBINSTANCE_VALIDATION_ERROR.getMessageObject());
        } else if (id.equals("") || host.equals("") || sid.equals("")) {
            errors.add(Messages.DBINSTANCE_VALIDATION_ERROR.getMessageObject());
        }
        errors.addAll(new UserValidatorImpl().validate(user));
        return errors;
    }
}
