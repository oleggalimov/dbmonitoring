package org.oleggalimov.dbmonitoring.back.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.influxdb.InfluxDB;
import org.mockito.Mockito;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseUser;
import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
import org.oleggalimov.dbmonitoring.back.enumerations.Role;
import org.oleggalimov.dbmonitoring.back.enumerations.UserStatus;
import org.oleggalimov.dbmonitoring.back.repositories.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.naming.NamingException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Configuration
public class TestBeans {
    private Logger LOGGER = LoggerFactory.getLogger(TestBeans.class);

    @Bean
    public Map<String, String> getJNDIValues(@Autowired List<String> jndiKeys) {
        Map<String, String> result = new HashMap<>();
        if (jndiKeys.isEmpty()) {
            LOGGER.debug("Список имен пуст! List<String>: {}", jndiKeys);
            return result;
        }
        final JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiKeys.forEach(key -> {
            try {
                jndiObjectFactoryBean.setJndiName(key);
                jndiObjectFactoryBean.afterPropertiesSet();
                result.put(key, (String) jndiObjectFactoryBean.getObject());
            } catch (NamingException e) {
                LOGGER.debug("Не удалось найти значение JNDI: {}", key);
                e.printStackTrace();
            }
        });
        return result;
    }


    @Bean
    public MonitoringSystemUser getDefaultSystemUser(@Autowired BCryptPasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode("passw0rd");
        return new MonitoringSystemUser(
                "Admin", "noreply@domain.com", EnumSet.allOf(Role.class), "John", "Doe", "987", password, UserStatus.ACTIVE
        );
    }


    @Bean
    public CopyOnWriteArraySet<DataBaseInstance> instanceHashSet() {
        CopyOnWriteArraySet<DataBaseInstance> instances = new CopyOnWriteArraySet<>();
        instances.add(new DataBaseInstance("test", "host", 1520, "sid", "testDatabase", new DataBaseUser("login", "password"), DatabaseInstanceType.ORACLE));
        return instances;
    }

    @Bean
    public ResponseBuilder getResponseBuilder() {
        return new ResponseBuilder(new ObjectMapper());
    }


    //security
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //JndiConf
    @Bean
    public List<String> getJndiObjectKeys() {
        return Collections.singletonList("java:/dbmonitoring/dropuserdb");
    }

    @Bean
    IUserRepository userRepository() {
        return Mockito.mock(IUserRepository.class);
    }

    //Influx
    @Bean
    InfluxDB getInfluxDb() {
        return Mockito.mock(InfluxDB.class);
    }
}
