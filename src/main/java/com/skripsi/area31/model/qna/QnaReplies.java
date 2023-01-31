package com.skripsi.area31.model.qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class QnaReplies {
    @Id String idReplies;
    private String idUser;
    private String name;
    private String idComment;
    private String body;
    private Long createdAt;
    private Long updatedAt;
}
