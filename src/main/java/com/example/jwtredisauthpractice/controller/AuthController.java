package com.example.jwtredisauthpractice.controller;

import com.example.jwtredisauthpractice.config.JwtProperties;
import com.example.jwtredisauthpractice.config.TokenProvider;
import com.example.jwtredisauthpractice.domain.User;
import com.example.jwtredisauthpractice.dto.LoginRequest;
import com.example.jwtredisauthpractice.dto.TokenResponse;
import com.example.jwtredisauthpractice.service.RefreshTokenService;
import com.example.jwtredisauthpractice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest req) {
        User user = userService.findByEmail(req.email());
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        String at = tokenProvider.generateToken(user.getId(), user.getEmail(), Duration.ofMinutes(15));
        String rt = tokenProvider.generateToken(user.getId(), user.getEmail(), Duration.ofDays(14));
        refreshTokenService.save(user.getId(), rt, Duration.ofDays(14));
        return new TokenResponse(at, rt);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody Map<String, String> body) {
        String clientRt = body.get("refreshToken");
        if (clientRt == null) throw new IllegalArgumentException("refreshToken 필요");

        Long userId = tokenProvider.getUserId(clientRt);
        String storedRt = refreshTokenService.get(userId);
        if (storedRt == null || !storedRt.equals(clientRt)) {
            throw new IllegalStateException("로그아웃됐거나 토큰이 변경됨. 다시 로그인하세요.");
        }
        String email = tokenProvider.getClaims(clientRt).getSubject();

        String newAT = tokenProvider.generateToken(userId, email, Duration.ofMinutes(15));
        String newRT = tokenProvider.generateToken(userId, email, Duration.ofDays(14));
        refreshTokenService.save(userId, newRT, Duration.ofDays(14));

        return new TokenResponse(newAT, newRT);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody Map<String, String> body) {
        String clientRt = body.get("refreshToken");
        if (clientRt == null) throw new IllegalArgumentException("refreshToken 필요");
        Long userId = tokenProvider.getUserId(clientRt);
        refreshTokenService.delete(userId);
    }

}
