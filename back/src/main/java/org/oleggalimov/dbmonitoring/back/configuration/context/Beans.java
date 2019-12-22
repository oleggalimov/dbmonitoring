package org.oleggalimov.dbmonitoring.back.configuration.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseUser;
import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
import org.oleggalimov.dbmonitoring.back.enumerations.Role;
import org.oleggalimov.dbmonitoring.back.enumerations.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jndi.JndiTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.naming.NamingException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Configuration
public class Beans {
    private Logger LOGGER = LoggerFactory.getLogger(Beans.class);

    @Bean
    public Map<String, String> getJNDIValues() {
        List<String> jndiKeys = Arrays.asList(
                "java:/dbmonitoring/dropuserdb"
        );
        Map<String, String> result = new HashMap<>();
        JndiTemplate jndi = new JndiTemplate();
        jndiKeys.forEach(key -> {
            try {
                result.put(key, (String) jndi.lookup(key));
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
        instances.add(new DataBaseInstance("test", "host", 1520, "sid", new DataBaseUser("login", "password"), DatabaseInstanceType.ORACLE));
        return instances;
    }
    @Bean
    public ResponseBuilder getResponseBuilder() {
        return new ResponseBuilder(new ObjectMapper());
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost");
    }

    @Bean
    public MongoTemplate mongoTemplate(@Autowired MongoClient client, @Autowired MonitoringSystemUser defaultSystemUser, @Autowired Map<String, String> jndi) {
        MongoTemplate myDatabase = new MongoTemplate(client, "mydatabase");
        boolean dropCollection = true;
        String key = "java:/dbmonitoring/dropuserdb";
        if (!jndi.isEmpty() && jndi.containsKey(key)) {
            dropCollection = Boolean.parseBoolean(jndi.get(key));
        }
        if (dropCollection) {
            LOGGER.debug("Сбрасываем коллекцию Users");
            myDatabase.dropCollection("Users");
            myDatabase.save(defaultSystemUser);
        }
        return myDatabase;
    }

    //security
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
