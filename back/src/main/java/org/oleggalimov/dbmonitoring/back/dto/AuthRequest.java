package org.oleggalimov.dbmonitoring.back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequest {
    String login;
    String password;
}
