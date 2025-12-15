package com.example.clothingstore.service;

import com.example.clothingstore.entity.User;
import com.example.clothingstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String username, String email, String password) {

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    public User login(String username, String rawPassword) {

        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) return null;

        return passwordEncoder.matches(rawPassword, user.getPasswordHash())
                ? user
                : null;
    }
}
