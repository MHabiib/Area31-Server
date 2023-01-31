package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class Quiz {
    @Id String idQuiz;
    private String idCourse;
    private String idQuestionBank;
    private String title;
    private String description;
    private String status;
    private Long duration;
    private Long startDate;
    private Long endDate;
}
