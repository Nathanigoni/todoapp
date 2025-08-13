package com.example.todo.controller;

import com.example.todo.model.User;
import com.example.todo.payload.AuthRequest;
import com.example.todo.payload.AuthResponse;
import com.example.todo.repository.UserRepository;
import com.example.todo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody AuthRequest request) {
        String login = request.getLogin();
        boolean isEmail = login.contains("@");

        if (isEmail) {
            if (userRepository.existsByEmail(login)) {
                return ResponseEntity.badRequest().body("Email is already taken");
            }
        } else {
            if (userRepository.existsByUsername(login)) {
                return ResponseEntity.badRequest().body("Username is already taken");
            }
        }

        User user = new User();
        if (isEmail) {
            user.setEmail(login);
            user.setUsername(login.split("@")[0]); // Optional: derive username from email
        } else {
            user.setUsername(login);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody AuthRequest request) {
        try {
            // Authenticate using username or email
            User user = userRepository.findByEmail(request.getLogin())
                    .or(() -> userRepository.findByUsername(request.getLogin()))
                    .orElseThrow(() -> new RuntimeException("Invalid username/email or password"));

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword())
            );

            // Generate JWT
            String token = jwtUtil.generateToken(authentication.getName());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username/email or password");
        }
    }
}
