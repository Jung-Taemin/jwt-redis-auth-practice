package com.example.jwtredisauthpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final StringRedisTemplate stringRedisTemplate;

    private String key(Long userId) {
        return "RT:" + userId;
    }

    public void save(Long userId, String refreshToken, Duration ttl) {
        stringRedisTemplate.opsForValue().set(key(userId), refreshToken, ttl);
    }

    public String get(Long userId) {
        return stringRedisTemplate.opsForValue().get(key(userId));
    }

    public void delete(Long userId) {
        stringRedisTemplate.delete(key(userId));
    }
}
