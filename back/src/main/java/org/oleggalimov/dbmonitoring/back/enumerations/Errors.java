package org.oleggalimov.dbmonitoring.back.enumerations;

import org.oleggalimov.dbmonitoring.back.dto.Error;

public enum Errors {
    SERVICE_EXCEPTION(new Error("00", "Critical error", "Service not available")),

    USER_VALIDATION_EMPTY_USER(new Error("U_01", "User info is invalid", "User info is empty")),
    USER_VALIDATION_EMPTY_LOGIN(new Error("U_02", "User info is invalid", "Login is empty")),
    USER_VALIDATION_EMPTY_PASSWORD(new Error("U_03", "User info is invalid", "Password is empty")),
    USER_VALIDATION_SHORT_PASSWORD(new Error("U_04", "User info is invalid", "Password is too short")),
    USER_VALIDATION_BAD_EMAIL(new Error("U_05", "User info is invalid", "E-mail address is malformed")),

    DB_USER_VALIDATION_EMPTY_USER(new Error("DBU_01", "User info is invalid", "User info is empty")),
    DB_USER_VALIDATION_EMPTY_LOGIN(new Error("DBU_02", "Database user info is invalid", "Login is empty")),
    DB_USER_VALIDATION_EMPTY_PASSWORD(new Error("DBU_03", "Database user info is invalid", "Password is empty")),
    DB_USER_VALIDATION_SHORT_PASSWORD(new Error("DBU_04", "Database user info is invalid", "Password is too short")),

    DB_INSTANCE_VALIDATION_EMPTY_INSTANCE(new Error("DBI_01", "Database instance info is invalid", "Instance is empty")),
    DB_INSTANCE_VALIDATION_EMPTY_ID(new Error("DBI_02", "Database instance info is invalid", "Id is empty")),
    DB_INSTANCE_VALIDATION_EMPTY_HOST(new Error("DBI_03", "Database instance info is invalid", "Host is empty")),
    DB_INSTANCE_VALIDATION_EMPTY_SID(new Error("DBI_04", "Database instance info is invalid", "SID is empty")),
    DB_INSTANCE_VALIDATION_EMPTY_PORT(new Error("DBI_05", "Database instance info is invalid", "Port is empty")),
    DB_INSTANCE_VALIDATION_EMPTY_TYPE(new Error("DBI_06", "Database instance info is invalid", "Type is empty")),

    AWR_INSTANCE_NOT_FOUND(new Error("AWR_01", "No such instance", "Instance is not found")),
    AWR_WRONG_INSTANCE_TYPE(new Error("AWR_02", "Wrong type", "AWR supported only with Oracle!")),
    AWR_CONNECT_ERROR(new Error("AWR_03", "Connection error", "Can't connect to instance")),
    AWR_NOT_FOUND(new Error("AWR_03", "No AWR report", "Awr for period is not found!")),
    AWR_NO_DATA(new Error("AWR_03", "No AWR report", "Can't select data for AWR"));
    private Error error;

    Errors(Error err) {
        this.error = err;
    }

    public Error getError() {
        return error;
    }
}
