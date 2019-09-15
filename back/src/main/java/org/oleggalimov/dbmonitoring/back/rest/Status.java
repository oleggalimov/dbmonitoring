package org.oleggalimov.dbmonitoring.back.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Status {
    @GetMapping (path = "/status")
    public String getStatus() {
        return "Back is Ok!";
    }
}
