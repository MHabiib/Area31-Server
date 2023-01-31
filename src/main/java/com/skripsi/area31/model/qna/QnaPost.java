package com.skripsi.area31.model.qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class QnaPost {
    @Id String idPost;
    private String idUser;
    private String name;
    private String idCourse;
    private String courseName;
    private String title;
    private String body;
    private Long createdAt;
    private Long updatedAt;
    private Integer totalComment = 0;
}
