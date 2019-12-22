package org.oleggalimov.dbmonitoring.back.repositories;

import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserRepository<T extends MonitoringSystemUser> extends MongoRepository<T, String> {
    @Query("{ 'login' : ?0 }")
    T findByLogin(String login);

    @Query("{ 'eMail' : ?0 }")
    T findByEmail(String eMail);

    Long countByLogin(String login);
}
