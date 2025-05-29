package com.example.SpringSecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the public page!";
    }

    @GetMapping("/secure")
    public String secure() {
        return "This is a secured endpoint!";
    }
    
    @GetMapping("/user/dashboard")
    public String userDashboard() {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Hello "+authentication.getName()+" Welcome to User Dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "Admin Dashboard";
    }
}
