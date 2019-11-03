package org.oleggalimov.dbmonitoring.back.repositories;

import org.oleggalimov.dbmonitoring.back.dto.CommonUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserRepository<T extends CommonUser> extends MongoRepository<T, String> {
    @Query("{ 'login' : ?0 }")
    T findByLogin(String login);

    @Query("{ 'eMail' : ?0 }")
    T findByEmail(String eMail);

}
