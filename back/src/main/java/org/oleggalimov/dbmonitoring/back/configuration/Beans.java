package org.oleggalimov.dbmonitoring.back.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.oleggalimov.dbmonitoring.back.builders.ResponceBuilder;
import org.oleggalimov.dbmonitoring.back.dto.implementations.DbInstanceImpl;
import org.oleggalimov.dbmonitoring.back.dto.implementations.UserImpl;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CopyOnWriteArraySet;

@Configuration
public class Beans {

    @Bean
    public CopyOnWriteArraySet<CommonDbInstance> instanceHashSet() {
        CopyOnWriteArraySet<CommonDbInstance> instances = new CopyOnWriteArraySet<>();
        instances.add(new DbInstanceImpl("test", "host", 1520, "sid", new UserImpl("login", "password")));
        return instances;
    }

    @Bean
    public ResponceBuilder getResponceBuilder() {
        return new ResponceBuilder(new ObjectMapper());
    }
}
