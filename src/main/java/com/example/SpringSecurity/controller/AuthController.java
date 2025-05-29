package com.example.SpringSecurity.controller;

import java.util.Collections;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringSecurity.model.AuthRequest;
import com.example.SpringSecurity.model.RegisterRequest;
import com.example.SpringSecurity.model.Role;
import com.example.SpringSecurity.model.User;
import com.example.SpringSecurity.repository.RoleRepository;
import com.example.SpringSecurity.repository.UserRepository;
import com.example.SpringSecurity.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepo;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, UserRepository userRepo, PasswordEncoder passwordEncoder, RoleRepository roleRepo, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    	
    	Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @SuppressWarnings("unchecked")
	@PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return "Username already taken";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        
        Role defaultRole = roleRepo.findByName("ROLE_DEFAULT")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        
        ((Set<Role>) user.getAuthorities()).add(defaultRole);


        userRepo.save(user);
        return "User registered successfully!";
    }
}
