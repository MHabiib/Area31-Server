package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class ComplaintResponse {
    private String idComplaint;
    private String quizTitle;
    private String status;
    private String description;
    private String reason;
    private Long quizDate;
    private Long assignedAt;
    private Integer score;
}
