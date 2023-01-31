package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class Complaint {
    @Id String idComplaint;
    private String quizTitle;
    private String idUser;
    private String idCourse;
    private String idInstructor;
    private String idReport;
    private String description;
    private Long createdAt;
    private String status;
    private String reason;
    private String fcm;
}
