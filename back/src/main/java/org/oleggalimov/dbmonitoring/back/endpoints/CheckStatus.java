package org.oleggalimov.dbmonitoring.back.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController

public class CheckStatus {
    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @GetMapping(path = "/status")
    public String getStatus() {
        String[] activeProfiles = environment.getActiveProfiles();
        return "Back is Ok! ActiveProfiles: " + Arrays.toString(activeProfiles);
    }
}
