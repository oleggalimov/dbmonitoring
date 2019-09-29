package org.oleggalimov.dbmonitoring.back.enumerations;

import org.oleggalimov.dbmonitoring.back.dto.implementations.MessageImpl;

public enum Messages {
    //exception
    SERVICE_EXCEPTION(new MessageImpl("01", "Critical error", "Service not avaliable", MessageType.EXCEPTION)),
    //error
    DBINSTANCE_VALIDATION_ERROR(new MessageImpl("02", "Instance info is invalid", "Some of critical fields (id, host, sid) are empty", MessageType.ERROR)),
    USER_VALIDATION_ERROR(new MessageImpl("03", "User info is invalid", "Login or password are empty", MessageType.ERROR)),
    //info
    DBINSTANCE_CREATED(new MessageImpl("04", "Success", "Instance was successfully added.", MessageType.INFO));

    private MessageImpl messageObject;

    Messages(MessageImpl msg) {
        this.messageObject = msg;
    }

    public MessageImpl getMessageObject() {
        return messageObject;
    }
}
