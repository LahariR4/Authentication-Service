package com.assignment.authentication.service;

import com.assignment.authentication.dao.AuthResponse;
import com.assignment.authentication.dao.Role;
import com.assignment.authentication.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;
    private String secret = "ags@#2344552"; // Should be in application properties

    @Autowired
    private PasswordEncoder passwordEncoder;  // Spring Security's PasswordEncoder


    public String generateToken(String username, String password) {

        //fetch role from userName
        Optional<User> user = userService.findByUsername(username);

        if(user.isPresent()) {
            User fetchedUser = user.get();
            String passwordHash = passwordEncoder.encode(password);
            String passwordSaved = fetchedUser.getPasswordHash();
            //validate password
            if (passwordHash.equals(passwordSaved)) {
                List<Role> role = fetchedUser.getRoles();
                return Jwts.builder()
                        .setSubject(username)
                        .claim("role", role)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();
            }
        }
        //throw not found exception
        return null;
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = getUsernameFromToken(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public Boolean isAuthorisedRole(String token, String role) {
        final String extractedRole = getRoleFromToken(token);
        return extractedRole.contains(role);
    }

    public Boolean validateTokenAndRole(String token, String username, String role){
        final String extractedUsername = getUsernameFromToken(token);
        return this.validateToken(token, username) && this.isAuthorisedRole(token, role);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    public Boolean isTokenExpired(String token) {
        return getClaimFromToken(token, Claims::getExpiration).before(new Date());
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

}


