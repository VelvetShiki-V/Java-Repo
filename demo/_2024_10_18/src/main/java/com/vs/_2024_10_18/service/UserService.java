package com.vs._2024_10_18.service;

import com.vs._2024_10_18.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserFromDB() {
        return User.builder()
                .uid("1")
                .role("admin")
                .username("shiki")
                .password(passwordEncoder.encode("123"))
                .email("984.com")
                .phone("666")
                .status(true)
                .create_time(LocalDateTime.now())
                .update_time(LocalDateTime.now())
                .build();
    }
}
