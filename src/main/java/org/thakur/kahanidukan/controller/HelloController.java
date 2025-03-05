package org.thakur.kahanidukan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thakur.kahanidukan.models.Message;

@RestController
public class HelloController {
    @GetMapping
    public Message hello() {
        return new Message("App is running");
    }
}
