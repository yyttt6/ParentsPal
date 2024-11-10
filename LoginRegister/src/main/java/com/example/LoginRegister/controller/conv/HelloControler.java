package com.example.demo.controller.conv;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloControler {
    @GetMapping("/test/hello")
    public String hello() {
        return "Hello world !";
    }
}
