package com.skripsi.area31.model.course;


import com.skripsi.area31.model.tesaurus.Tesaurus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class QuestionFulfilled {
    private String idQuestion;
    private String question;
    private Set<String> answer;
    private String questionType;
    private String answerKey;
    private String[] steamedAnswerKey;
    private Integer score;
    private List<Tesaurus> synonymAnswerKey;
}
