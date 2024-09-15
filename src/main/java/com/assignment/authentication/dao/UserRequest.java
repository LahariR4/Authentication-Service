package com.assignment.authentication.dao;

// UserRequest.java
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserRequest {
    // Getters and setters
    private String username;
    private String password;
    private List<Role> roles;

}
