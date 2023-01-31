package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class ComplaintPageInstructor {
    private String idComplaint;
    private String quizTitle;
    private String name;
    private String description;
    private String course;
    private Integer score;
    private String status;
}
