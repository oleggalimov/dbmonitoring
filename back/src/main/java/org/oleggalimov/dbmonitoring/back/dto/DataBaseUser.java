package org.oleggalimov.dbmonitoring.back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataBaseUser {
    private String login;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private transient String password;

}
