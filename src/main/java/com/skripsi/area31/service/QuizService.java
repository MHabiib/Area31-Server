package com.skripsi.area31.service;

import com.skripsi.area31.model.course.AnsweredQuestion;
import com.skripsi.area31.model.course.Quiz;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

public interface QuizService {
    ResponseEntity createQuiz(Quiz quiz);

    ResponseEntity updateQuiz(Quiz quiz);

    ResponseEntity deleteQuiz(String id_quiz);

    ResponseEntity listQuizStudent(String id_course, Integer page, Principal principal);

    ResponseEntity startQuizStudent(String id_quiz, Principal principal);

    ResponseEntity submitQuiz(String fcm, String id_quiz,
        Map<Integer, AnsweredQuestion> answeredQuestion, Principal principal) throws IOException;

    ResponseEntity createComplaint(String fcm, String id_quiz, String complaint,
        Principal principal);

    ResponseEntity updateComplaint(String idComplaint, String status, String reason,
        Map<String, Integer> updatedScore);

    ResponseEntity listQuizInstructor(String id_course, Integer page, Principal principal);

    ResponseEntity listReportInstructor(String id_quiz, Principal principal);

    ResponseEntity reportDetailsInstructor(String id_report);
}
