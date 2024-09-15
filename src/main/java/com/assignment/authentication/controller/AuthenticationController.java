package com.assignment.authentication.controller;


import com.assignment.authentication.dao.AuthRequest;
import com.assignment.authentication.dao.AuthResponse;
import com.assignment.authentication.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/token")
public class AuthenticationController {


    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthRequest authRequest) {
        String token = authenticationService.generateToken(authRequest.getUsername(), authRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/verify-authorise")
    public ResponseEntity<Boolean> registerUser(@PathVariable String token,@PathVariable String userName,@PathVariable String role) {
        boolean  isValid = authenticationService.validateTokenAndRole(token, userName, role);
        return ResponseEntity.ok(isValid);
    }



}


