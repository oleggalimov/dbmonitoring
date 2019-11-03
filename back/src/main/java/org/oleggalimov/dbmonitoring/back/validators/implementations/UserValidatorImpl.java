package org.oleggalimov.dbmonitoring.back.validators.implementations;

import org.oleggalimov.dbmonitoring.back.dto.User;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.validators.interfaces.UserValidator;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class UserValidatorImpl implements UserValidator<User> {
    @Override
    public List<Serializable> validate(User user) {
        if (user == null) {
            return Collections.singletonList(Messages.USER_VALIDATION_ERROR.getMessageObject());
        }
        String login = user.getLogin();
        String password = user.getPassword();
        String email = user.getEmail();

        if (login != null && !login.equals("") && password != null && !password.equals("") && email != null && !email.equals("")) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(Messages.USER_VALIDATION_ERROR.getMessageObject());
        }
    }
}
