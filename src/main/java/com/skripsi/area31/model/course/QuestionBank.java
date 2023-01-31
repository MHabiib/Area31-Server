package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class QuestionBank {
    @Id String idQuestionBank;
    private String idInstructor;
    private String title;
    private String description;
    private Set<String> listIdQuestion;
}
