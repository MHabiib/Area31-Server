package com.skripsi.area31.service;

import com.skripsi.area31.model.course.QuestionBankRequest;
import com.skripsi.area31.model.course.UpdateQuestionBankRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.Principal;

public interface QuestionService {
    ResponseEntity createQuestionBank(QuestionBankRequest questionBankRequest, Principal principal)
        throws IOException;

    ResponseEntity updateQuestionBank(UpdateQuestionBankRequest questionBankRequest,
        String idQuestionBank, Principal principal) throws IOException;

    ResponseEntity deleteQuestionBank(String idQuestionBank);

    ResponseEntity getQuestionBankList(Integer page, Principal principal);

    ResponseEntity getQuestionDetails(String idQuestionBank);
}
