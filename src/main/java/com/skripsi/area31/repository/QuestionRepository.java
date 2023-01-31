package com.skripsi.area31.repository;

import com.skripsi.area31.model.course.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository public interface QuestionRepository extends MongoRepository<Question, String> {
    Question findQuestionByIdQuestion(String idQuestion);
}
