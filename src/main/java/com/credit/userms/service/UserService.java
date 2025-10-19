package com.credit.userms.service;

import com.credit.userms.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface UserService {
    ResponseEntity<User> getUserByUsername(String username);
    ResponseEntity<List<User>> getAllUsers();
    String deleteUser(String username);
    String setPasswordAtUserServiceImpl(String newPassword, String userEmail, PasswordEncoder passwordEncoder);
}
