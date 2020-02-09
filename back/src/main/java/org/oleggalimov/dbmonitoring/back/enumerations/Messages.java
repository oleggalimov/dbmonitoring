package org.oleggalimov.dbmonitoring.back.enumerations;

import org.oleggalimov.dbmonitoring.back.dto.Message;

public enum Messages {
    DB_INSTANCE_CREATED(new Message("DBI_I_01", "Success instance operation: create", "Instance was successfully added", MessageType.INFO)),
    DB_INSTANCE_UPDATED(new Message("DBI_I_02", "Success instance operation: update", "Instance was successfully updated", MessageType.INFO)),
    DB_INSTANCE_DELETED(new Message("DBI_I_03", "Success instance operation: delete", "Instance was successfully deleted", MessageType.INFO)),

    DB_INSTANCE_IS_ABSENT(new Message("DBI_W_01", "Read instance error", "No instance was found in list", MessageType.WARNING)),
    DB_INSTANCE_IS_NOT_DELETED(new Message("DBI_W_02", "Delete instance error", "Instance was not deleted", MessageType.WARNING)),
    DB_INSTANCE_IS_NOT_ADDED(new Message("DBI_W_03", "Add instance error", "Instance was not added", MessageType.WARNING)),
    DB_INSTANCE_IS_NOT_UPDATED(new Message("DBI_W_04", "Update instance error", "Instance was not updated", MessageType.WARNING)),
    DB_INSTANCE_IS_ALREADY_EXIST(new Message("DBI_W_05", "Add instance error", "Instance with such id already exists", MessageType.WARNING)),

    USER_CREATED(new Message("U_I_01", "Success user operation: create", "User was successfully added", MessageType.INFO)),
    USER_UPDATED(new Message("U_I_02", "Success user operation: update", "User was successfully updated", MessageType.INFO)),
    USER_DELETED(new Message("U_I_03", "Success user operation: delete", "User was successfully deleted", MessageType.INFO)),

    USER_IS_NOT_ADDED(new Message("U_W_01", "Add user error", "User was not added", MessageType.WARNING)),
    USER_IS_ABSENT(new Message("U_W_02", "Read user error", "No user was found in database", MessageType.WARNING)),
    USER_IS_NOT_DELETED(new Message("U_W_03", "Delete user error", "User was not deleted", MessageType.WARNING)),
    USER_IS_NOT_UPDATED(new Message("U_W_04", "Update instance error", "User was not updated", MessageType.WARNING)),

    INFLUX_NO_DATA_RETURNED(new Message("I_W_01", "Read points error", "Result set is null", MessageType.WARNING)),

    AUTH_EMPTY_DATA(new Message("A_W_01", "Authorisation error", "Login or password is empty", MessageType.WARNING)),
    AUTH_LOGIN_ERROR(new Message("A_W_02", "Authorisation error", "No such user or wrong password", MessageType.WARNING)),
    AUTH_ACC_LOCKED(new Message("A_W_03", "Authorisation error", "Account is locked", MessageType.WARNING));

    private Message messageObject;

    Messages(Message msg) {
        this.messageObject = msg;
    }

    public Message getMessageObject() {
        return messageObject;
    }
}
