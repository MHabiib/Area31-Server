package com.skripsi.area31.model.qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class QnaComment {
    @Id String idComment;
    private String idUser;
    private String name;
    private String idPost;
    private String body;
    private Long createdAt;
    private Long updatedAt;
    private Integer totalReplies = 0;
}
