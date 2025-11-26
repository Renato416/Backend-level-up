package com.backendLevelup.Backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // O el puerto donde corre tu frontend
public class TestController {

    @GetMapping("/api/test")
    public String test() {
        return "Backend funcionando";
    }
}
