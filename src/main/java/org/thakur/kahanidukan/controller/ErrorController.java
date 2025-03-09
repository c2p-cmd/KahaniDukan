package org.thakur.kahanidukan.controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ErrorController {
    @GetMapping(path = "/error")
    public Map<String, String> handleException(@RequestBody Map<String, Object> requestBody, RuntimeException e) {
        return Collections.singletonMap("error", "An error occurred: " + e.getMessage());
    }
}
