package org.oleggalimov.dbmonitoring.back.dto;

import org.oleggalimov.dbmonitoring.back.enumerations.Roles;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "Users")
public class User extends CommonUser {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String personNumber;
    private transient String password;
    private transient Set<Roles> roles;

    public User() {
    }

    public User(String login, String eMail, Set<Roles> roles, String id, String firstName, String lastName, String personNumber, String password, Set<Roles> roles1) {
        super(login, eMail, roles);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personNumber = personNumber;
        this.password = password;
        this.roles = roles1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    public boolean addRole(Roles anotherRole) {
        return this.roles.add(anotherRole);
    }

    public boolean removeRole(Roles oldRole) {
        return this.roles.remove(oldRole);
    }

    public boolean hasRole(Roles role) {
        return this.roles.contains(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", personNumber='" + personNumber + '\'' +
                '}';
    }
}
