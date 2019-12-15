package org.oleggalimov.dbmonitoring.back.validators;

import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.entities.User;
import org.oleggalimov.dbmonitoring.back.enumerations.Errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserValidator {
    public static List<Error> validate(User user) {
        List<Error> errors = new ArrayList<>();
        if (user == null) {
            return Collections.singletonList(Errors.USER_VALIDATION_EMPTY_USER.getError());
        }
        String login = user.getLogin();
        String password = user.getPassword();
        String email = user.getEMail();

        if (login == null || StringValidator.isEmpty(login)) {
            errors.add(Errors.USER_VALIDATION_EMPTY_LOGIN.getError());
        }
        if (password == null || StringValidator.isEmpty(password)) {
            errors.add(Errors.USER_VALIDATION_EMPTY_PASSWORD.getError());
        }
        if (password != null && password.length() < 8) {
            errors.add(Errors.USER_VALIDATION_SHORT_PASSWORD.getError());
        }
        if (email == null || StringValidator.isInValidEMail(email)) {
            errors.add(Errors.USER_VALIDATION_BAD_EMAIL.getError());
        }
        return errors;
    }
}
