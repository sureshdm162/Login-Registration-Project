package com.credit.userms.controller;

import com.credit.userms.entity.User;
import com.credit.userms.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("test")
    public String test(Authentication authentication){
        System.out.println("Inside /users/test");
        System.out.println("Logged in user: " + authentication.getName());
        return "Testing";
    }

    // Get current authenticated user's info
    @GetMapping("me")
    public ResponseEntity<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Extract username from JWT
        return userServiceImpl.getUserByUsername(username);
    }

    // Admin-only: Get all users
    @GetMapping("allUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return userServiceImpl.getAllUsers();
    }

    // Admin-only: Delete user
    @DeleteMapping("username")
    public String deleteUser(String username){
        return userServiceImpl.deleteUser(username );
    }
}