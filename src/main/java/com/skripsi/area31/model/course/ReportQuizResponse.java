package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class ReportQuizResponse {
    private String title;
    private String description;
    private Long duration;
    private Long quizDate;
    private Long assignAt;
    private Integer score;
    private List<ReportQuestion> reportQuizResponses;
}
