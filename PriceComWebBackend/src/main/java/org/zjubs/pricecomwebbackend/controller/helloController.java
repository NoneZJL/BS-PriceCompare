package org.zjubs.pricecomwebbackend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zjubs.pricecomwebbackend.query.RespResult;

@RestController
public class helloController {
    @GetMapping("/hello")
    public RespResult hello() {
        String err = "Hello Spring Boot";
        return RespResult.success(err);
    }
}
