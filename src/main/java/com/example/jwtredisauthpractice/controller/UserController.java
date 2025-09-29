package com.example.jwtredisauthpractice.controller;

import com.example.jwtredisauthpractice.domain.User;
import com.example.jwtredisauthpractice.dto.RegisterRequest;
import com.example.jwtredisauthpractice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody @Valid RegisterRequest req) {
        return userService.register(req.email(), req.password(), req.nickname());
    }
}
