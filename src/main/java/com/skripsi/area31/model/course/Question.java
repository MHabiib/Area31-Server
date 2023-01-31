package com.skripsi.area31.model.course;


import com.skripsi.area31.model.tesaurus.Tesaurus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class Question {
    @Id String idQuestion;
    private String question;
    private Set<String> answer;
    private String questionType;
    private String answerKey;
    private Integer score;
    private String[] steamedAnswerKey;
    private List<Tesaurus> synonymAnswerKey;
}
