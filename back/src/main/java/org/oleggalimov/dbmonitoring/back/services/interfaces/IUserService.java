package org.oleggalimov.dbmonitoring.back.services.interfaces;

import org.oleggalimov.dbmonitoring.back.dto.CommonUser;

import java.util.List;

public interface IUserService<T extends CommonUser> {

    T findUserByLogin(String userLogin);

    T findUserById(String id);

    T findByEmail(String eMail);

    List<T> findAll();

    T saveUser(T user) throws Exception;

    void deleteUser(T user);

    boolean updateUser(T user);

}
