package com.skripsi.area31.repository;

import com.skripsi.area31.model.tesaurus.Tesaurus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository public interface TesaurusRepository extends MongoRepository<Tesaurus, String> {
    Tesaurus findTop1ByMainWord(String mainWord);
}
