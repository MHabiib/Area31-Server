package com.skripsi.area31.service.impl;

import com.skripsi.area31.model.user.User;
import com.skripsi.area31.repository.UserRepository;
import com.skripsi.area31.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service public class UserServiceImpl implements UserService {
    @Autowired UserRepository userRepository;
    @Override public ResponseEntity createUser(User user) {
        userRepository.save(user);
        return new ResponseEntity<>("Update user successful !", HttpStatus.OK);
    }

    @Override public ResponseEntity getUser() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
