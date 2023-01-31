package com.skripsi.area31.controller;

import com.skripsi.area31.model.course.AnsweredQuestion;
import com.skripsi.area31.model.course.Quiz;
import com.skripsi.area31.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@CrossOrigin("**") @RestController @RequestMapping public class QuizController {
    @Autowired QuizService quizService;

    @PostMapping("api/instructor/quiz/create")
    public ResponseEntity createQuiz(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @PutMapping("api/instructor/quiz/update")
    public ResponseEntity updateQuiz(@RequestBody Quiz quiz) {
        return quizService.updateQuiz(quiz);
    }

    @DeleteMapping("api/instructor/quiz/delete") public ResponseEntity deleteQuiz(String id_quiz) {
        return quizService.deleteQuiz(id_quiz);
    }

    @GetMapping("api/student/quiz")
    public ResponseEntity listQuizStudent(String id_course, Integer page, Principal principal) {
        return quizService.listQuizStudent(id_course, page, principal);
    }

    @GetMapping("api/student/quiz/start")
    public ResponseEntity startQuizStudent(String id_quiz, Principal principal) {
        return quizService.startQuizStudent(id_quiz, principal);
    }

    @PutMapping("api/student/quiz/submit")
    public ResponseEntity submitQuiz(String fcm, String id_quiz,
        @RequestBody Map<Integer, AnsweredQuestion> answeredQuestion, Principal principal)
        throws IOException {
        return quizService.submitQuiz(fcm, id_quiz, answeredQuestion, principal);
    }

    @PostMapping("api/student/quiz/complaint")
    public ResponseEntity createComplaint(String fcm, String id_quiz, String complaint,
        Principal principal) {
        return quizService.createComplaint(fcm, id_quiz, complaint, principal);
    }

    @PutMapping("api/instructor/quiz/complaint")
    public ResponseEntity updateComplaint(String idComplaint, String status, String reason,
        @RequestBody(required = false) Map<String, Integer> updatedScore) {
        return quizService.updateComplaint(idComplaint, status, reason, updatedScore);
    }

    @GetMapping("api/instructor/quiz")
    public ResponseEntity listQuizInstructor(String id_course, Integer page, Principal principal) {
        return quizService.listQuizInstructor(id_course, page, principal);
    }

    @GetMapping("api/instructor/report") public ResponseEntity listReportInstructor(String id_quiz,
        Principal principal) {
        return quizService.listReportInstructor(id_quiz, principal);
    }

    @GetMapping("api/instructor/report/details")
    public ResponseEntity reportDetailsInstructor(String id_report) {
        return quizService.reportDetailsInstructor(id_report);
    }
}
