package org.oleggalimov.dbmonitoring.back.endpoints;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Login {
    @PostMapping("/login")
    public String login() {
        return "Login page";
    }
}
