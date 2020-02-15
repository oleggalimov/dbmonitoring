package org.oleggalimov.dbmonitoring.back.services;

import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.oleggalimov.dbmonitoring.back.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private IUserRepository<MonitoringSystemUser> userRepository;

    @Autowired
    public UserService(IUserRepository<MonitoringSystemUser> userRepository) {
        this.userRepository = userRepository;
    }

    public MonitoringSystemUser findUserByLogin(String userLogin) {
        return userRepository.findByLogin(userLogin);
    }

    public MonitoringSystemUser findUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public MonitoringSystemUser findByEmail(String eMail) {
        return userRepository.findByEmail(eMail);
    }

    public List<MonitoringSystemUser> findAll() {
        return userRepository.findAll();
    }

    public MonitoringSystemUser saveUser(MonitoringSystemUser monitoringSystemUser) throws Exception {
        if (userRepository.findByLogin(monitoringSystemUser.getLogin()) != null) {
            throw new Exception("User with such login already exists");
        } else {
            return userRepository.save(monitoringSystemUser);
        }

    }

    public long deleteUser(MonitoringSystemUser monitoringSystemUser) {
        userRepository.delete(monitoringSystemUser);
        return userRepository.countByLogin(monitoringSystemUser.getLogin());
    }

    public boolean updateUser(MonitoringSystemUser monitoringSystemUser) {
        MonitoringSystemUser oldMonitoringSystemUser;
        oldMonitoringSystemUser = userRepository.findByLogin(monitoringSystemUser.getLogin());
        if (oldMonitoringSystemUser != null) {
            userRepository.delete(oldMonitoringSystemUser);
            userRepository.save(monitoringSystemUser);
            return true;
        }
        return false;
    }
}
