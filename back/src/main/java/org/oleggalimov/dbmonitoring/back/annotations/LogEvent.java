package org.oleggalimov.dbmonitoring.back.annotations;

import org.oleggalimov.dbmonitoring.back.enumerations.EventTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogEvent {
    public EventTypes eventType();
    public String message();

}
