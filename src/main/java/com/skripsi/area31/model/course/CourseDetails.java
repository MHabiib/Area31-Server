package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class CourseDetails {
    private String name;
    private String description;
    private String instructorName;
    private int totalMembers;
    private String idUser;
}
