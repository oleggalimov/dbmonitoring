package org.oleggalimov.dbmonitoring.back.configuration.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.implementations.DbInstanceImpl;
import org.oleggalimov.dbmonitoring.back.dto.implementations.DbUserImpl;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.CopyOnWriteArraySet;

@Configuration
public class Beans {

    @Bean
    public CopyOnWriteArraySet<CommonDbInstance> instanceHashSet() {
        CopyOnWriteArraySet<CommonDbInstance> instances = new CopyOnWriteArraySet<>();
        instances.add(new DbInstanceImpl("test", "host", 1520, "sid", new DbUserImpl("login", "password")));
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
    public MongoTemplate mongoTemplate(@Autowired MongoClient client) {
        return new MongoTemplate(client, "mydatabase");
    }

}
