package com.skripsi.area31.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class Chapter {
    @Id String idChapter;
    private String title;
    private String description;
    private String lectureNote;
    private String idCourse;
    private Long createdDate;
}
