package com.skripsi.area31.service;

import com.skripsi.area31.model.user.User;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

public interface UserService {
    ResponseEntity createUser(User user);

    ResponseEntity getUser();

    ResponseEntity logout(Principal principal);

    ResponseEntity getProfile(Principal principal);

    ResponseEntity updateUser(User user, Principal principal, String new_password);

    ResponseEntity isAuthorize(String role, Principal principal);

    ResponseEntity forgotPassword(String email) throws MessagingException;

    ResponseEntity forgotPasswordNextStep(String email, Integer code);

    ResponseEntity resetPassword(String email, String password, Integer code);

    ResponseEntity instructorDashboard(Principal principal);

    ResponseEntity email(String name, String email, String message)
        throws MessagingException, UnsupportedEncodingException;
}
