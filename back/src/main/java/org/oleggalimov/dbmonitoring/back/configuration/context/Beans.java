package org.oleggalimov.dbmonitoring.back.configuration.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jndi.JndiTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.naming.NamingException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

@Configuration
@PropertySources({
        @PropertySource("classpath:influx.properties")
})
public class Beans {
    private Logger LOGGER = LoggerFactory.getLogger(Beans.class);

    //DB instances
    @Bean
    public CopyOnWriteArraySet<DataBaseInstance> instanceHashSet() {
        CopyOnWriteArraySet<DataBaseInstance> instances = new CopyOnWriteArraySet<>();
        instances.add(
                new DataBaseInstance("test", "host", 1520, "sid", "testDataBase",
                        new DataBaseUser("login", "password"), DatabaseInstanceType.ORACLE));
        return instances;
    }

    //User repository (Mongo)
    @Bean
    public MonitoringSystemUser getDefaultSystemUser(@Autowired BCryptPasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode("passw0rd");
        return new MonitoringSystemUser(
                "Admin", "noreply@domain.com", EnumSet.allOf(Role.class), "John", "Doe", "987", password, UserStatus.ACTIVE
        );
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

    //jndi
    @Bean
    public Map<String, String> getJNDIValues(@Autowired List<String> keys) {
        Map<String, String> result = new HashMap<>();
        if (keys.isEmpty()) {
            LOGGER.debug("Список ключей пуст");
            return result;
        }
        JndiTemplate jndi = new JndiTemplate();
        keys.forEach(key -> {
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
    public List<String> getJNDIKeys() {
        return Collections.singletonList(
                "java:/dbmonitoring/dropuserdb"
        );
    }

    //response utils
    @Bean
    public ResponseBuilder getResponseBuilder() {
        return new ResponseBuilder(new ObjectMapper());
    }

    //influxDB

    @Bean
    InfluxDB getInfluxDBInstance(
            @Value("${connection.url}") String connectURl,
            @Value("${connection.username}") String user,
            @Value("${connection.password}") String pass,
            @Value("${batching.byCount}") int batchByCount,
            @Value("${batching.byTimeInMs}") int batchByTime
    ) {
        InfluxDB influxBean = InfluxDBFactory.connect(connectURl, user, pass);
        influxBean.enableBatch(batchByCount, batchByTime, TimeUnit.MILLISECONDS);
        return influxBean;
    }

}
