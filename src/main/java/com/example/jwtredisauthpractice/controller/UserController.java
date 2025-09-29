package com.example.jwtredisauthpractice.controller;

import com.example.jwtredisauthpractice.domain.User;
import com.example.jwtredisauthpractice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String nickname) {
        return userService.register(email, password, nickname);
    }
}
