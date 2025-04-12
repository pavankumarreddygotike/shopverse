package com.userservice.controller;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.userservice.model.User;
import com.userservice.security.JwtUtil;
import com.userservice.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    public UserController(UserService service, JwtUtil jwtUtil) {
        this.userService = service;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User saved = userService.register(user);
        System.out.println("Trigger Sonar");
        return ResponseEntity.ok(saved);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> u = userService.login(user.getUsername(), user.getPassword());
        if (u.isPresent()) {
            String token = jwtUtil.generateToken(u.get().getUsername());
            System.out.println("Trigger EC2");
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}