package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Set;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class Course {
    @Id String idCourse;
    private String name;
    private String description;
    private String idInstructor;
    private String instructorName;
    private Set<String> idStudent;
    private HashMap<String, String> resources;
    private String courseId;
    private String coursePassword;
    private String status;
    private String qr;
}
