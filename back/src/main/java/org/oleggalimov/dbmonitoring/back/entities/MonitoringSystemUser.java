package org.oleggalimov.dbmonitoring.back.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oleggalimov.dbmonitoring.back.enumerations.Role;
import org.oleggalimov.dbmonitoring.back.enumerations.UserStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Users")
public class MonitoringSystemUser {

    public MonitoringSystemUser(String login, String eMail, Set<Role> roles, String firstName, String lastName, String personNumber, String password, UserStatus status) {
        this.login = login;
        this.eMail = eMail;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personNumber = personNumber;
        this.password = password;
        this.status = status;
    }

    @Id
    @JsonIgnore
    private String id;
    private String login;
    private String eMail;
    private Set<Role> roles;
    private String firstName;
    private String lastName;
    private String personNumber;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private UserStatus status;

}
