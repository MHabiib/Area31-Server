package com.skripsi.area31.repository;

import com.skripsi.area31.model.course.QuestionFulfilled;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository public interface QuestionFulfilledRepository
    extends MongoRepository<QuestionFulfilled, String> {
    QuestionFulfilled findQuestionByIdQuestion(String idQuestion);
}
