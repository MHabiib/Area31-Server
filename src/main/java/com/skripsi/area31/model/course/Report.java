package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class Report {
    @Id String idReport;
    private String idStudent;
    private String idCourse;
    private String idQuiz;
    private Long quizDate;
    private Long quizDuration;
    private Long assignedAt;
    private Integer score;
    private String titleQuiz;
    private String descriptionQuiz;
    private List<AnsweredQuestion> answeredQuestionList;
}
