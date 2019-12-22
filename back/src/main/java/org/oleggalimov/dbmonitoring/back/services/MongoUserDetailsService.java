package org.oleggalimov.dbmonitoring.back.services;

import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MongoUserDetailsService implements UserDetailsService {
    private UserService userService;

    @Autowired
    public MongoUserDetailsService(UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        MonitoringSystemUser monitoringSystemUser = userService.findUserByLogin(login);
        if (monitoringSystemUser != null && !monitoringSystemUser.getRoles().isEmpty()) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            monitoringSystemUser.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.name()));
            });
            return new User(monitoringSystemUser.getLogin(), monitoringSystemUser.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
