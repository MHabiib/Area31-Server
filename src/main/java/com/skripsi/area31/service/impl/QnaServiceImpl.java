package com.skripsi.area31.service.impl;

import com.skripsi.area31.model.course.Course;
import com.skripsi.area31.model.qna.QnaComment;
import com.skripsi.area31.model.qna.QnaPost;
import com.skripsi.area31.model.qna.QnaReplies;
import com.skripsi.area31.model.response.SimpleCustomResponse;
import com.skripsi.area31.model.user.User;
import com.skripsi.area31.repository.*;
import com.skripsi.area31.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

@Service(value = "qnaService") public class QnaServiceImpl implements QnaService {
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired QnaPostRepository qnaPostRepository;
    @Autowired QnaCommentRepository qnaCommentRepository;
    @Autowired QnaRepliesRepository qnaRepliesRepository;

    @Override public ResponseEntity postQna(String id_course, String title, String body,
        Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Course course = courseRepository.findCourseByIdCourse(id_course);
        QnaPost qnaPost = new QnaPost();
        qnaPost.setBody(body);
        qnaPost.setTitle(title);
        qnaPost.setName(user.getName());
        qnaPost.setIdUser(user.getIdUser());
        qnaPost.setCreatedAt(Calendar.getInstance().getTimeInMillis());
        qnaPost.setIdCourse(id_course);
        qnaPost.setCourseName(course.getName());
        qnaPostRepository.save(qnaPost);
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Success post qna"),
            HttpStatus.OK);
    }

    @Override public ResponseEntity updatePostQna(String id_post, String title, String body) {
        QnaPost qnaPost = qnaPostRepository.findQnaPostByIdPost(id_post);
        qnaPost.setUpdatedAt(Calendar.getInstance().getTimeInMillis());
        qnaPost.setTitle(title);
        qnaPost.setBody(body);
        qnaPostRepository.save(qnaPost);
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Success update post qna"),
            HttpStatus.OK);
    }

    @Override public ResponseEntity deletePostQna(String id_post) {
        QnaPost qnaPost = qnaPostRepository.findQnaPostByIdPost(id_post);
        List<QnaComment> qnaCommentList = qnaCommentRepository.findAllByIdPost(qnaPost.getIdPost());
        for (QnaComment comment : qnaCommentList) {
            List<QnaReplies> qnaRepliesList =
                qnaRepliesRepository.findAllByIdComment(comment.getIdComment());
            for (QnaReplies qnaReplies : qnaRepliesList) {
                qnaRepliesRepository.delete(qnaReplies);
            }
            qnaCommentRepository.delete(comment);
        }
        qnaPostRepository.delete(qnaPost);

        return new ResponseEntity<>(new SimpleCustomResponse(200, "Success delete post"),
            HttpStatus.OK);
    }

    @Override
    public ResponseEntity postCommentStudent(String id_post, String body, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        QnaPost qnaPost = qnaPostRepository.findQnaPostByIdPost(id_post);

        QnaComment qnaComment = new QnaComment();
        qnaComment.setBody(body);
        qnaComment.setName(user.getName());
        qnaComment.setIdUser(user.getIdUser());
        qnaComment.setCreatedAt(Calendar.getInstance().getTimeInMillis());
        qnaComment.setIdPost(id_post);
        qnaPost.setTotalComment(qnaPost.getTotalComment() + 1);
        qnaCommentRepository.save(qnaComment);
        qnaPostRepository.save(qnaPost);

        return ResponseEntity.ok(qnaComment);
    }

    @Override
    public ResponseEntity postRepliesStudent(String id_comment, String body, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        QnaComment qnaComment = qnaCommentRepository.findQnaCommentByIdComment(id_comment);

        QnaReplies qnaReplies = new QnaReplies();
        qnaReplies.setBody(body);
        qnaReplies.setName(user.getName());
        qnaReplies.setIdUser(user.getIdUser());
        qnaReplies.setCreatedAt(Calendar.getInstance().getTimeInMillis());
        qnaReplies.setIdComment(id_comment);
        qnaComment.setTotalReplies(qnaComment.getTotalReplies() + 1);
        qnaCommentRepository.save(qnaComment);
        qnaRepliesRepository.save(qnaReplies);

        return ResponseEntity.ok(qnaReplies);
    }

    @Override
    public ResponseEntity getCoursePost(Integer page, String id_course, Principal principal) {
        PageRequest request = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (userRepository.findByEmail(principal.getName()).getRole().equals("ROLE_INSTRUCTOR")
            && id_course == null) {
            return ResponseEntity.ok(qnaPostRepository
                .findAllByIdUser(userRepository.findByEmail(principal.getName()).getIdUser(),
                    request));
        } else {
            return ResponseEntity.ok(qnaPostRepository.findAllByIdCourse(id_course, request));
        }
    }

    @Override
    public ResponseEntity getPostComment(Integer page, String id_post, Principal principal) {
        PageRequest request = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(qnaCommentRepository.findAllByIdPost(id_post, request));
    }

    @Override
    public ResponseEntity getCommentReplies(Integer page, String id_comment, Principal principal) {
        PageRequest request = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(qnaRepliesRepository.findAllByIdComment(id_comment, request));
    }

    @Override public ResponseEntity deleteCommentStudent(String id_comment, Principal principal) {
        QnaComment comment = qnaCommentRepository.findQnaCommentByIdComment(id_comment);
        QnaPost post = qnaPostRepository.findQnaPostByIdPost(comment.getIdPost());
        List<QnaReplies> repliesList =
            qnaRepliesRepository.findAllByIdComment(comment.getIdComment());

        post.setTotalComment(post.getTotalComment() - 1);
        qnaPostRepository.save(post);

        for (QnaReplies qnaReplies : repliesList) {
            qnaRepliesRepository.delete(qnaReplies);
        }

        qnaCommentRepository.delete(comment);
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Comment deleted"),
            HttpStatus.OK);
    }

    @Override public ResponseEntity deleteRepliesStudent(String id_replies, Principal principal) {
        QnaReplies qnaReplies = qnaRepliesRepository.findQnaRepliesByIdReplies(id_replies);
        QnaComment qnaComment =
            qnaCommentRepository.findQnaCommentByIdComment(qnaReplies.getIdComment());

        qnaComment.setTotalReplies(qnaComment.getTotalReplies() - 1);
        qnaCommentRepository.save(qnaComment);

        qnaRepliesRepository.delete(qnaReplies);

        return new ResponseEntity<>(new SimpleCustomResponse(200, "Replies deleted"),
            HttpStatus.OK);
    }

    @Override public ResponseEntity updateCommentStudent(String id_comment, String body,
        Principal principal) {
        QnaComment comment = qnaCommentRepository.findQnaCommentByIdComment(id_comment);
        comment.setBody(body);
        comment.setUpdatedAt(Calendar.getInstance().getTimeInMillis());
        qnaCommentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }

    @Override public ResponseEntity updateRepliesStudent(String id_replies, String body,
        Principal principal) {
        QnaReplies replies = qnaRepliesRepository.findQnaRepliesByIdReplies(id_replies);
        replies.setBody(body);
        replies.setUpdatedAt(Calendar.getInstance().getTimeInMillis());
        qnaRepliesRepository.save(replies);
        return ResponseEntity.ok(replies);
    }
}
