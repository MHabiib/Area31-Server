package com.skripsi.area31.service;

import com.skripsi.area31.model.user.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity createUser(User user);

    ResponseEntity getUser();
}
