package org.oleggalimov.dbmonitoring.back.validators.implementations;

import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbUser;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.validators.interfaces.UserDbValidator;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class UserDbValidatorImpl implements UserDbValidator {
    @Override
    public List<Serializable> validate(CommonDbUser user) {
        if (user == null) {
            return Collections.singletonList(Messages.USER_DB_VALIDATION_ERROR.getMessageObject());
        }
        String login = user.getLogin();
        String password = user.getPassword();

        if (login != null && !login.equals("") && password != null && !password.equals("")) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(Messages.USER_DB_VALIDATION_ERROR.getMessageObject());
        }
    }
}
