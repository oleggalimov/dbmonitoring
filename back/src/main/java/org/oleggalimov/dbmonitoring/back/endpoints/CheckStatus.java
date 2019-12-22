package org.oleggalimov.dbmonitoring.back.endpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckStatus {
    @GetMapping(path = "/status")
    public String getStatus() {
        return "Back is Ok!";
    }
}