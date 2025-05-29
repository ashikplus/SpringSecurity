package com.example.SpringSecurity.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringSecurity.model.Role;
import com.example.SpringSecurity.model.RoleAssignmentRequest;
import com.example.SpringSecurity.model.User;
import com.example.SpringSecurity.repository.RoleRepository;
import com.example.SpringSecurity.repository.UserRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public AdminController(UserRepository userRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/assign-role")
    public ResponseEntity<String> assignRoleToUser(@RequestBody RoleAssignmentRequest request) {
    	
    	System.out.println("coming....");
        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Role role = roleRepo.findByName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        if (user.getAuthorities().contains(role)) {
            return ResponseEntity.badRequest().body("User already has this role");
        }

        ((Set<Role>) user.getAuthorities()).add(role); 
        userRepo.save(user);

        return ResponseEntity.ok("Role assigned successfully");
    }
}
