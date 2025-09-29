package com.example.jwtredisauthpractice.service;

import com.example.jwtredisauthpractice.domain.User;
import com.example.jwtredisauthpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String email, String rawPassword, String nickname) {
        if (userRepository.existsByEmail(email))
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .nickname(nickname)
                .build();
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
    }
}
