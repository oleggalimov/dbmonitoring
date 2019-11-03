package org.oleggalimov.dbmonitoring.back.enumerations;

import org.oleggalimov.dbmonitoring.back.dto.implementations.MessageImpl;

public enum Messages {
    //exception
    SERVICE_EXCEPTION(new MessageImpl("01", "Critical error", "Service not avaliable", MessageType.EXCEPTION)),
    //error
    DBINSTANCE_VALIDATION_ERROR(new MessageImpl("02", "Instance info is invalid", "Some of critical fields (id, host, sid) are empty", MessageType.ERROR)),
    USER_DB_VALIDATION_ERROR(new MessageImpl("03", "DB User info is invalid", "Login or password are empty", MessageType.ERROR)),
    USER_VALIDATION_ERROR(new MessageImpl("04", "User info is invalid", "Login or password are empty", MessageType.ERROR)),
    //info
    DBINSTANCE_CREATED(new MessageImpl("05", "Success", "Instance was successfully added.", MessageType.INFO)),
    DBINSTANCE_UPDATED(new MessageImpl("06", "Success", "Instance was successfully updated.", MessageType.INFO)),
    DBINSTANCE_DELETED(new MessageImpl("07", "Success", "Instance was successfully deleted.", MessageType.INFO)),
    //warning
    DBINSTANCE_IS_ABSENT(new MessageImpl("08", "Read instance error", "No instance was found in list.", MessageType.WARNING)),
    DBINSTANCE_IS_NOT_DELETED(new MessageImpl("09", "Delete instance error", "Instance was not deleted due to error", MessageType.WARNING));
    private MessageImpl messageObject;

    Messages(MessageImpl msg) {
        this.messageObject = msg;
    }

    public MessageImpl getMessageObject() {
        return messageObject;
    }
}
