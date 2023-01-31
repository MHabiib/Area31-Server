package com.skripsi.area31.controller;

import com.skripsi.area31.model.user.User;
import com.skripsi.area31.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

@CrossOrigin("**") @RestController @RequestMapping public class UserController {
    @Autowired UserService userService;
    @Autowired PasswordEncoder passwordEncoder;

    @PostMapping("api/user") public ResponseEntity createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.createUser(user);
    }

    @PutMapping("api/student/user")
    public ResponseEntity updateUser(Principal principal, @RequestBody User user,
        String new_password) {
        return userService.updateUser(user, principal, new_password);
    }

    @PutMapping("api/instructor/user")
    public ResponseEntity updateUserInstructor(Principal principal, @RequestBody User user,
        String new_password) {
        return userService.updateUser(user, principal, new_password);
    }

    @GetMapping("api/student/profile") public ResponseEntity profile(Principal principal) {
        return userService.getProfile(principal);
    }

    @GetMapping("api/instructor/profile")
    public ResponseEntity profileInstructor(Principal principal) {
        return userService.getProfile(principal);
    }

    @GetMapping("api/instructor/dashboard")
    public ResponseEntity instructorDashboard(Principal principal) {
        return userService.instructorDashboard(principal);
    }

    @GetMapping("/isAuthorize")
    public ResponseEntity isAuthorize(String role, Principal principal) {
        return userService.isAuthorize(role, principal);
    }

    @PostMapping("logout-account") public ResponseEntity logout(Principal principal) {
        return userService.logout(principal);
    }

    @PostMapping("forgot-password") public ResponseEntity forgotPassword(String email)
        throws MessagingException {
        return userService.forgotPassword(email);
    }

    @PostMapping("forgot-password/code")
    public ResponseEntity forgotPasswordNextStep(String email, Integer code) {
        return userService.forgotPasswordNextStep(email, code);
    }

    @PostMapping("forgot-password/resetPassword")
    public ResponseEntity resetPassword(String email, String password, Integer code) {
        return userService.resetPassword(email, password, code);
    }

    @PostMapping("email")
    public ResponseEntity email(String name, String email, String message)
        throws MessagingException, UnsupportedEncodingException {
        return userService.email(name, email, message);
    }

    @GetMapping("api/admin/user") public ResponseEntity getUser() {
        return userService.getUser();
    }
}
