package org.oleggalimov.dbmonitoring.back.validators;

import org.oleggalimov.dbmonitoring.back.dto.DataBaseUser;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.enumerations.Errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBaseUserValidator {
    private DataBaseUserValidator() {
    }

    public static List<Error> validate(DataBaseUser user) {
        List<Error> errors = new ArrayList<>();
        if (user == null) {
            return Collections.singletonList(Errors.DB_USER_VALIDATION_EMPTY_USER.getError());
        }

        String login = user.getLogin();
        String password = user.getPassword();

        if (login == null || StringValidator.isEmpty(login)) {
            errors.add(Errors.DB_USER_VALIDATION_EMPTY_LOGIN.getError());
        }
        if (password == null || StringValidator.isEmpty(password)) {
            errors.add(Errors.DB_USER_VALIDATION_EMPTY_PASSWORD.getError());
        }
        if (password != null && password.length() < 8) {
            errors.add(Errors.DB_USER_VALIDATION_SHORT_PASSWORD.getError());
        }
        return errors;

    }
}
