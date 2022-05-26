package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class HealthCheckController {

    @GetMapping("/eb")
    public String healthCheck() {
        return "엘라스틱 빈 확인중";
    }
}
