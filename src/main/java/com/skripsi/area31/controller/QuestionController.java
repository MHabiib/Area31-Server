package com.skripsi.area31.controller;

import com.skripsi.area31.model.course.QuestionBankRequest;
import com.skripsi.area31.model.course.UpdateQuestionBankRequest;
import com.skripsi.area31.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@CrossOrigin("**") @RestController @RequestMapping public class QuestionController {
    @Autowired QuestionService questionService;

    @PostMapping("api/instructor/question-bank/create")
    public ResponseEntity createQuestionBank(@RequestBody QuestionBankRequest questionBankRequest,
        Principal principal) throws IOException {
        return questionService.createQuestionBank(questionBankRequest, principal);
    }

    @GetMapping("/api/instructor/question-bank")
    public ResponseEntity getQuestionBankList(Integer page, Principal principal) {
        return questionService.getQuestionBankList(page, principal);
    }

    @PutMapping("api/instructor/question-bank/update") public ResponseEntity updateQuestionBank(
        @RequestBody UpdateQuestionBankRequest questionBankRequest, String idQuestionBank,
        Principal principal) throws IOException {
        return questionService.updateQuestionBank(questionBankRequest, idQuestionBank, principal);
    }

    @DeleteMapping("api/instructor/question-bank/delete")
    public ResponseEntity deleteQuestionBank(String idQuestionBank) {
        return questionService.deleteQuestionBank(idQuestionBank);
    }

    @GetMapping("/api/instructor/question")
    public ResponseEntity getQuestionDetails(String idQuestionBank) {
        return questionService.getQuestionDetails(idQuestionBank);
    }
}
