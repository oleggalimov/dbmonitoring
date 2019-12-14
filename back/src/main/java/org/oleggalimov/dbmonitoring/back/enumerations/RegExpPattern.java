package org.oleggalimov.dbmonitoring.back.enumerations;

import java.util.regex.Pattern;

public enum RegExpPattern {
    NOT_EMPTY_STRING(Pattern.compile("^(?!\\s*$).+")),
    VALID_EMAIL(Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"));
    private Pattern pattern;

    RegExpPattern(Pattern expression) {
        this.pattern = expression;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
