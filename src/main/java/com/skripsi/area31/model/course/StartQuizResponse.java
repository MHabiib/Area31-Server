package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class StartQuizResponse {
    private String title;
    private String description;
    private Long duration;
    private Long startDate;
    private List<Question> questionList;
}
