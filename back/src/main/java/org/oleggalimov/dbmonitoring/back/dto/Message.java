package org.oleggalimov.dbmonitoring.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oleggalimov.dbmonitoring.back.enumerations.MessageType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String code;
    private String title;
    private String message;
    private MessageType type;
}
