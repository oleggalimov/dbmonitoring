package org.oleggalimov.dbmonitoring.back.services;

import org.oleggalimov.dbmonitoring.back.entities.User;
import org.oleggalimov.dbmonitoring.back.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private IUserRepository<User> userRepository;

    @Autowired
    public UserService(IUserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByLogin(String userLogin) {
        return userRepository.findByLogin(userLogin);
    }

    public User findUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String eMail) {
        return userRepository.findByEmail(eMail);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User saveUser(User user) throws Exception {
        if (userRepository.findByEmail(user.getEMail()) != null) {
            throw new Exception("User with such e-mail already exists");
        } else if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new Exception("User with such login already exists");
        } else {
            return userRepository.save(user);
        }

    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public boolean updateUser(User user) {
        User oldUser;
        oldUser = userRepository.findByEmail(user.getEMail());
        if (oldUser == null) {
            oldUser = userRepository.findByLogin(user.getLogin());
        }
        if (oldUser != null) {
            userRepository.delete(oldUser);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
