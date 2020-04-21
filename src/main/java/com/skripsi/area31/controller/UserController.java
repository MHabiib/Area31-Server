package com.skripsi.area31.controller;

import com.skripsi.area31.model.user.User;
import com.skripsi.area31.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping public class UserController {
    @Autowired UserService userService;
    @Autowired PasswordEncoder passwordEncoder;

    @PostMapping("/user") public ResponseEntity createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.createUser(user);
    }

    @GetMapping("api2/user") public ResponseEntity getUser() {
        return userService.getUser();
    }
}
