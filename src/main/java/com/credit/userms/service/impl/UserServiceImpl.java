package com.credit.userms.service.impl;

import com.credit.userms.entity.User;
import com.credit.userms.repository.UserRepository;
import com.credit.userms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<User> getUserByUsername(String username) {
       User userDetails= userRepository.findByUsername(username).get();
        ResponseEntity<User> userEntity=new ResponseEntity<>(userDetails, HttpStatus.OK);
        return userEntity;
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users=userRepository.findAll();
        ResponseEntity<List<User>> allUsers=new ResponseEntity<>(users,HttpStatus.OK);
        return allUsers;
    }

    @Override
    public String deleteUser(String username) {
         userRepository.deleteByUsername(username);
         return "User deleted successfully!";
    }

    //Reset password
    @Override
    public String setPasswordAtUserServiceImpl(String newPassword, String userEmail,PasswordEncoder passwordEncoder) {
        try {
            User user=userRepository.findByEmail(userEmail);
            user.setPassword(passwordEncoder.encode(newPassword));
            String username=user.getUsername();
            userRepository.save(user);
            return "Password reset is successful for user:"+username;
        }catch (Exception e){
            e.getStackTrace();
            return "Error occurred while saving the new password for user";
        }

    }
}
