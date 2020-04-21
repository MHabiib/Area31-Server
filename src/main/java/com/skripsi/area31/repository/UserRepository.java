package com.skripsi.area31.repository;

import com.skripsi.area31.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAll();

    User findByEmail(String email);

    User findByIdUser(String idUser);

    Integer countByEmail(String email);
}
