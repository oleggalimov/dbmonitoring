package org.oleggalimov.dbmonitoring.back.validators;

import org.oleggalimov.dbmonitoring.back.enumerations.RegExpPattern;

public class StringValidator {
    public static boolean isEmpty(String testedString) {
        return !RegExpPattern.NOT_EMPTY_STRING.getPattern().matcher(testedString).matches();
    }

    static boolean isInValidEMail(String testedEMail) {
        return !RegExpPattern.VALID_EMAIL.getPattern().matcher(testedEMail).matches();
    }
}
