package com.example.jwtredisauthpractice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MeController {
    @GetMapping("/me")
    public java.util.Map<String,Object> me(org.springframework.security.core.Authentication a){
        return java.util.Map.of("userId", a.getPrincipal());
    }
}