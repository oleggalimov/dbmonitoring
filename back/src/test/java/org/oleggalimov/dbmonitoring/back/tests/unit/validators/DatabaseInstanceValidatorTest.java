package org.oleggalimov.dbmonitoring.back.tests.unit.validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseUser;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
import org.oleggalimov.dbmonitoring.back.enumerations.Errors;
import org.oleggalimov.dbmonitoring.back.validators.DatabaseInstanceValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseInstanceValidatorTest {
    private static ObjectMapper mapper;
    private static DataBaseUser user;

    @BeforeAll
    static void init() {
        mapper = new ObjectMapper();
        user = new DataBaseUser("login", "password");
    }

    @Test
    void userTest() {
        List<Error> errorList;
        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance("id", "host", 1521, "testDatabase", new DataBaseUser("login", "password"), DatabaseInstanceType.ORACLE));
        assertEquals(0, errorList.size());

        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance("id", "host", 1521, "testDatabase", null, DatabaseInstanceType.ORACLE));
        validateError(Errors.DB_USER_VALIDATION_EMPTY_USER, errorList);
    }

    @Test
    void idTest() {
        List<Error> errorList;
        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance("", "host", 1521, "testDatabase", user, DatabaseInstanceType.ORACLE));
        validateError(Errors.DB_INSTANCE_VALIDATION_EMPTY_ID, errorList);

        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance("  ", "host", 1521, "testDatabase", user, DatabaseInstanceType.ORACLE));
        validateError(Errors.DB_INSTANCE_VALIDATION_EMPTY_ID, errorList);

        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance(null, "host", 1521, "testDatabase", user, DatabaseInstanceType.ORACLE));
        validateError(Errors.DB_INSTANCE_VALIDATION_EMPTY_ID, errorList);

        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance(" 1", "host", 1521, "testDatabase", user, DatabaseInstanceType.ORACLE));
        assertEquals(0, errorList.size());
    }

    @Test
    void hostTest() {
        List<Error> errorList;
        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance("323", "   \t ", 1521, "testDatabase", user, DatabaseInstanceType.ORACLE));
        validateError(Errors.DB_INSTANCE_VALIDATION_EMPTY_HOST, errorList);

        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance("  1", "", 1521, "testDatabase", user, DatabaseInstanceType.ORACLE));
        validateError(Errors.DB_INSTANCE_VALIDATION_EMPTY_HOST, errorList);

        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance("null", null, 1521, "testDatabase", user, DatabaseInstanceType.ORACLE));
        validateError(Errors.DB_INSTANCE_VALIDATION_EMPTY_HOST, errorList);

        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance(" 1", "host", 1521, "testDatabase", user, DatabaseInstanceType.ORACLE));
        assertEquals(0, errorList.size());
    }

    @Test
    void portTest() {
        List<Error> errorList;
        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance("323", "  host \t ", null, "testDatabase", user, DatabaseInstanceType.ORACLE));
        validateError(Errors.DB_INSTANCE_VALIDATION_EMPTY_PORT, errorList);

        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance(" 1", "host", 1521, "testDatabase", user, DatabaseInstanceType.ORACLE));
        assertEquals(0, errorList.size());
    }

    @Test
    void typeTest() {
        List<Error> errorList;
        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance("323", "  host \t ", 1521,  "testDatabase", user, null));
        validateError(Errors.DB_INSTANCE_VALIDATION_EMPTY_TYPE, errorList);

        errorList = DatabaseInstanceValidator.validate(new DataBaseInstance(" 1", "host", 1521, "testDatabase", user, DatabaseInstanceType.ORACLE));
        assertEquals(0, errorList.size());
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
