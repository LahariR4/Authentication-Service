package com.assignment.authentication.service;

import com.assignment.authentication.dao.Role;
import com.assignment.authentication.entity.User;
import com.assignment.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Spring Security's PasswordEncoder

    public User saveUser(String username, String password, List<Role> roles) {
        String passwordHash = passwordEncoder.encode(password);  // Hash the password
        User user = new User(username, passwordHash, roles);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}

