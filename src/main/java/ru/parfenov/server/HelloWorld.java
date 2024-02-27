package ru.parfenov.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloWorld {
    @GetMapping("/hello")
    public String hello() {
        return "/WEB-INF/hello.jsp";
    }
}
