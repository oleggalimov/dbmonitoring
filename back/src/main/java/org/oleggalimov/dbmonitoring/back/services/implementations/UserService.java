package org.oleggalimov.dbmonitoring.back.services.implementations;

import org.oleggalimov.dbmonitoring.back.dto.User;
import org.oleggalimov.dbmonitoring.back.repositories.IUserRepository;
import org.oleggalimov.dbmonitoring.back.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService<User> {
    private IUserRepository<User> userRepository;

    @Autowired
    public UserService(IUserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByLogin(String userLogin) {
        return (User) userRepository.findByLogin(userLogin);
    }

    @Override
    public User findUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByEmail(String eMail) {
        return userRepository.findByEmail(eMail);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) throws Exception {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new Exception("User with such e-mail already exists");
        } else if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new Exception("User with such login already exists");
        } else {
            return userRepository.save(user);
        }

    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public boolean updateUser(User user) {
        User oldUser;
        oldUser = userRepository.findByEmail(user.getEmail());
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
