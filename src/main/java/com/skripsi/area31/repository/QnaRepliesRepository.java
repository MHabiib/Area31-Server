package com.skripsi.area31.repository;

import com.skripsi.area31.model.qna.QnaReplies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface QnaRepliesRepository extends MongoRepository<QnaReplies, String> {
    Page<QnaReplies> findAllByIdComment(String idComment, Pageable pageable);

    List<QnaReplies> findAllByIdComment(String idComment);

    QnaReplies findQnaRepliesByIdReplies(String idReplies);

    List<QnaReplies> findAllByIdUser(String idUser);
}
