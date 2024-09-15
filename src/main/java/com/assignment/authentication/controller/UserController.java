package com.assignment.authentication.controller;

import com.assignment.authentication.dao.UserRequest;
import com.assignment.authentication.dao.UserResponse;
import com.assignment.authentication.entity.User;
import com.assignment.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
        if (userService.existsByUsername(userRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }

        User user = userService.saveUser(userRequest.getUsername(), userRequest.getPassword(), userRequest.getRoles());
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getRoles()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new UserResponse(user.get().getId(), user.get().getUsername(), user.get().getRoles()));
    }
}

