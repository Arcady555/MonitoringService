package ru.parfenov.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RsstAnswer {
    @GetMapping("/helloRest")
    public String hello() {
        return "Hello!!!!!!!!!!!!!!";
    }
}
