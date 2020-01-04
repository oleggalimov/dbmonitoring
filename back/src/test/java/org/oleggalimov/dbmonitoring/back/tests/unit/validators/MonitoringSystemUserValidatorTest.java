package org.oleggalimov.dbmonitoring.back.tests.unit.validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.oleggalimov.dbmonitoring.back.enumerations.Errors;
import org.oleggalimov.dbmonitoring.back.enumerations.UserStatus;
import org.oleggalimov.dbmonitoring.back.validators.UserValidator;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonitoringSystemUserValidatorTest {
    private static ObjectMapper mapper;

    @BeforeAll
    static void init() {
        mapper = new ObjectMapper();
    }

    @Test
    void userValidatorLoginTest() {
        List<Error> errorList;

        errorList = UserValidator.validate(new MonitoringSystemUser("login", "e@mail.com", new HashSet<>(), "fName", "lName", "Number", "12345678", UserStatus.ACTIVE));
        assertEquals(0, errorList.size());

        errorList = UserValidator.validate(null);
        validateError(Errors.USER_VALIDATION_EMPTY_USER, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser("", null, null, null, null, null, null, null));
        validateError(Errors.USER_VALIDATION_EMPTY_LOGIN, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser("      ", null, null, null, null, null, null, null));
        validateError(Errors.USER_VALIDATION_EMPTY_LOGIN, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser(null, null, null, null, null, null, null, null));
        validateError(Errors.USER_VALIDATION_EMPTY_LOGIN, errorList);
    }

    @Test
    void userValidatorPasswordTest() {
        List<Error> errorList;
        errorList = UserValidator.validate(new MonitoringSystemUser("1", null, null, null, null, null, null, null));
        validateError(Errors.USER_VALIDATION_EMPTY_PASSWORD, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser(" 1 ", null, null, null, null, null, "", null));
        validateError(Errors.USER_VALIDATION_EMPTY_PASSWORD, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser("2", null, null, null, null, null, "  ", null));
        validateError(Errors.USER_VALIDATION_EMPTY_PASSWORD, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser("2", null, null, null, null, null, "  1", null));
        validateError(Errors.USER_VALIDATION_SHORT_PASSWORD, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser("2", "qwe@mail.ru", null, null, null, null, "       8", null));
        validateError(null, errorList);
    }

    @Test
    void userValidatorEMailTest() {
        List<Error> errorList;
        errorList = UserValidator.validate(new MonitoringSystemUser("1", null, null, null, null, null, "12345678", null));
        validateError(Errors.USER_VALIDATION_BAD_EMAIL, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser(" 1 ", "null", null, null, null, null, "12345678", null));
        validateError(Errors.USER_VALIDATION_BAD_EMAIL, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser("2", "    ", null, null, null, null, "12345678", null));
        validateError(Errors.USER_VALIDATION_BAD_EMAIL, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser("2", "     ", null, null, null, null, "12345678", null));
        validateError(Errors.USER_VALIDATION_BAD_EMAIL, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser("2", "@mail.ry", null, null, null, null, "12345678", null));
        validateError(Errors.USER_VALIDATION_BAD_EMAIL, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser("2", "1@mail.ry.", null, null, null, null, "12345678", null));
        validateError(Errors.USER_VALIDATION_BAD_EMAIL, errorList);

        errorList = UserValidator.validate(new MonitoringSystemUser("2", "qwe@mail.ru", null, null, null, null, "12345678", null));
        validateError(null, errorList);
    }

    private void validateError(Errors expected, List<Error> errorsList) {
        if (errorsList.size() != 0) {
            assertEquals(expected.getError(), errorsList.get(0));
        } else {
            if (expected != null) {
                Assertions.fail();
            }
        }
    }

}
