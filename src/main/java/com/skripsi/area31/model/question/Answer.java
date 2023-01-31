package com.skripsi.area31.model.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class Answer {
    private String idQuestion;
    private String idStudent;
    private String answer;
    private String answerKey; //for developonly next delete this
}
