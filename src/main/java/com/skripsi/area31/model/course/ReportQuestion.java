package com.skripsi.area31.model.course;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class ReportQuestion {
    private String idQuestionFulfilled;
    private String question;
    private Set<String> answer;
    private String questionType;
    private String answerKey;
    private String studentAnswer;
    private Integer score;
    private Integer studentScore;
    private Map<String, Ratio> ratioMap;
}
