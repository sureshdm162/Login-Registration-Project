package com.credit.userms.controller;

import com.credit.userms.entity.User;
import com.credit.userms.service.EmailService;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.credit.userms.dto.*;
import com.credit.userms.repository.UserRepository;
import com.credit.userms.config.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("auth")
    @RequiredArgsConstructor
    public class AuthController {

        private final AuthenticationManager authManager;
        private final UserRepository userRepo;
        private final JwtUtil jwtUtil;
        private final PasswordEncoder passwordEncoder;
        @Autowired
        private EmailService emailService;

        @PostMapping("register")
        public String registerUser(@RequestBody User user) {
            System.out.println("Hitting /auth/register endpoint....");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println("Received new user details....");
            //user.setStatus("ACTIVE");
            userRepo.save(user);
            System.out.println("User details saved to db....");
            //gqvqztjvmotdlqnu
            System.out.println("calling emailService....");
            return emailService.sendRegistrationEmail(user.getEmail(),user.getUsername());
        }

        @PostMapping("login")
        public JwtResponse login(@RequestBody JwtRequest jwtRequest) {
            //It returns Authentication object.
            //This line internally searches for a class which is implementing UserDetailsService, and it  will execute the override method "loadUserByUsername".
           Authentication authentication=  authManager.authenticate(
                    //authenticate methods needs an argument of type "UsernamePasswordAuthenticationToken", so we are creating it.
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );

            User user = userRepo.findByUsername(jwtRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(user.getUsername());
            return new JwtResponse(token, user.getRole().name());
        }
        //When this endpoint is hit, it sends the forgot password mail to user by taking user email id as input.
        @PostMapping("forgot-password")
        public ResponseEntity<String> forgotPassword(@RequestParam String userEmail){
                return new ResponseEntity<>(emailService.sendForgotPasswordMail(userEmail), HttpStatus.OK);
        }
        //when user clicks on the link which is sent via "forgot-password" endpoint, it will redirect the user to this endpoint.
        @PostMapping("set-password")
        public ResponseEntity<String> setPassword(@RequestBody PasswordRequest newPasswordObject,@RequestParam String userEmail){
            return new ResponseEntity<>(emailService.setPassword(newPasswordObject.getNewPassword(),userEmail,passwordEncoder),HttpStatus.OK);
        }
    }
//{
//  "username": "test",
//  "password": "test123",
//  "email":"test123@gmail.com",
//  "role":"STAFF"
//}
