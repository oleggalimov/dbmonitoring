package org.oleggalimov.dbmonitoring.back.unit.validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseUser;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.enumerations.Errors;
import org.oleggalimov.dbmonitoring.back.validators.DataBaseUserValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataBaseUserValidatorTest {
    private static ObjectMapper mapper;

    @BeforeAll
    static void init() {
        mapper = new ObjectMapper();
    }

    @Test
    void dataBaseUserAndPasswordLoginTest() {
        List<Error> errorList;
        errorList = DataBaseUserValidator.validate(new DataBaseUser("login", "12345678"));
        assertEquals(0, errorList.size());

        errorList = DataBaseUserValidator.validate(null);
        validateError(Errors.DB_USER_VALIDATION_EMPTY_USER, errorList);

        errorList = DataBaseUserValidator.validate(new DataBaseUser(null, "12345678"));
        validateError(Errors.DB_USER_VALIDATION_EMPTY_LOGIN, errorList);

        errorList = DataBaseUserValidator.validate(new DataBaseUser("   ", "12345678"));
        validateError(Errors.DB_USER_VALIDATION_EMPTY_LOGIN, errorList);

        errorList = DataBaseUserValidator.validate(new DataBaseUser("  ", "12345678"));
        validateError(Errors.DB_USER_VALIDATION_EMPTY_LOGIN, errorList);

        errorList = DataBaseUserValidator.validate(new DataBaseUser("   1", "    "));
        validateError(Errors.DB_USER_VALIDATION_EMPTY_PASSWORD, errorList);

        errorList = DataBaseUserValidator.validate(new DataBaseUser("   1", "   567"));
        validateError(Errors.DB_USER_VALIDATION_SHORT_PASSWORD, errorList);

        errorList = DataBaseUserValidator.validate(new DataBaseUser("   1", "1234567 "));
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
