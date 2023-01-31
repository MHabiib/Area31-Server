package com.skripsi.area31.repository;

import com.skripsi.area31.model.course.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface QuizRepository extends MongoRepository<Quiz, String> {

    List<Quiz> findQuizByIdCourseAndEndDateBefore(String idCourse, Long endDate);

    List<Quiz> findQuizByIdCourse(String idCourse);

    Quiz findQuizByIdQuiz(String idQuiz);

    Page<Quiz> findAllByIdCourse(String idCourse, Pageable pageable);

    List<Quiz> findAllByIdQuestionBank(String idQuestionBank);
}
