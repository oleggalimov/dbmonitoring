package controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Status {
    @GetMapping(path = "/status")
    public String getStatus() {
        return "Front is Ok!";
    }
}
