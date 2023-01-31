package com.skripsi.area31.repository;

import com.skripsi.area31.model.course.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface QuestionBankRepository extends MongoRepository<QuestionBank, String> {
    QuestionBank findQuestionBankByIdQuestionBank(String idQuestionBank);

    List<QuestionBank> findTop5ByIdInstructor(String idInstructor);

    Page<QuestionBank> findAllByIdInstructor(String idInstructor, Pageable pageable);
}
