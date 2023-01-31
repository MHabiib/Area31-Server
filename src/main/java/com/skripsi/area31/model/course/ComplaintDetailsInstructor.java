package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class ComplaintDetailsInstructor {
    private String idComplaint;
    private String idReport;
    private String studentName;
    private String description;
    private Integer score;
    private Long createdAt;
    private String status;
}
