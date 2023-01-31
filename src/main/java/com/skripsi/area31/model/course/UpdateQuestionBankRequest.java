package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class UpdateQuestionBankRequest {
    private String title;
    private String description;
    private Set<String> listDeletedIdQuestion;
    private List<Question> listQuestion;
}
