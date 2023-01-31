package com.skripsi.area31.repository;

import com.skripsi.area31.model.qna.QnaPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface QnaPostRepository extends MongoRepository<QnaPost, String> {
    QnaPost findQnaPostByIdPost(String idPost);

    Page<QnaPost> findAllByIdCourse(String idCourse, Pageable pageable);

    Page<QnaPost> findAllByIdUser(String idUser, Pageable pageable);

    List<QnaPost> findAllByIdCourse(String idCourse);
}
