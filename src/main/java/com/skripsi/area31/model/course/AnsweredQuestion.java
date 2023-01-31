package com.skripsi.area31.model.course;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class AnsweredQuestion {
    private String idQuestionFulfilled;
    private String studentAnswer;
    private Integer studentScore;
    private Map<String, Ratio> ratioMap;
}
