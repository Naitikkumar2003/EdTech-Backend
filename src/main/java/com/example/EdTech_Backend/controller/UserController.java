package com.example.EdTech_Backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @GetMapping("/user/userprofile")
    @PreAuthorize("hasRole('USER')")
    public String userprofile(){
        return "Welcome to user profile";
    }

    @GetMapping("admin/adminprofile")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminprofile(){
        return "Welcometo Admin Profile";
    }
}
