package com.skripsi.area31.repository;

import com.skripsi.area31.model.qna.QnaComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface QnaCommentRepository extends MongoRepository<QnaComment, String> {
    QnaComment findQnaCommentByIdComment(String idComment);

    Page<QnaComment> findAllByIdPost(String idPost, Pageable pageable);

    List<QnaComment> findAllByIdPost(String idPost);

    List<QnaComment> findAllByIdUser(String idUser);
}
