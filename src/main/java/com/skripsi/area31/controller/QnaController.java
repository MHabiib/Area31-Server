package com.skripsi.area31.controller;

import com.skripsi.area31.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin("**") @RestController @RequestMapping public class QnaController {
    @Autowired QnaService qnaService;

    @PostMapping("api/instructor/course/qna/post")
    public ResponseEntity postQna(String id_course, String title, String body,
        Principal principal) {
        return qnaService.postQna(id_course, title, body, principal);
    }

    @PutMapping("api/instructor/course/qna/post")
    public ResponseEntity updatePostQna(String id_post, String title, String body) {
        return qnaService.updatePostQna(id_post, title, body);
    }

    @DeleteMapping("api/instructor/course/qna/post")
    public ResponseEntity deletePostQna(String id_post) {
        return qnaService.deletePostQna(id_post);
    }

    @PostMapping("api/student/course/qna/comment")
    public ResponseEntity postCommentStudent(String id_post, String body, Principal principal) {
        return qnaService.postCommentStudent(id_post, body, principal);
    }

    @PutMapping("api/student/course/qna/comment")
    public ResponseEntity updateCommentStudent(String id_comment, String body,
        Principal principal) {
        return qnaService.updateCommentStudent(id_comment, body, principal);
    }

    @DeleteMapping("api/student/course/qna/comment")
    public ResponseEntity deleteCommentStudent(String id_comment, Principal principal) {
        return qnaService.deleteCommentStudent(id_comment, principal);
    }

    @PostMapping("api/student/course/qna/replies")
    public ResponseEntity postRepliesStudent(String id_comment, String body, Principal principal) {
        return qnaService.postRepliesStudent(id_comment, body, principal);
    }

    @PutMapping("api/student/course/qna/replies")
    public ResponseEntity updateRepliesStudent(String id_replies, String body,
        Principal principal) {
        return qnaService.updateRepliesStudent(id_replies, body, principal);
    }

    @DeleteMapping("api/student/course/qna/replies")
    public ResponseEntity deleteRepliesStudent(String id_replies, Principal principal) {
        return qnaService.deleteRepliesStudent(id_replies, principal);
    }

    @PostMapping("api/instructor/course/qna/comment")
    public ResponseEntity postCommentStudentInstructor(String id_post, String body,
        Principal principal) {
        return qnaService.postCommentStudent(id_post, body, principal);
    }

    @PutMapping("api/instructor/course/qna/comment")
    public ResponseEntity updateCommentStudentInstructor(String id_comment, String body,
        Principal principal) {
        return qnaService.updateCommentStudent(id_comment, body, principal);
    }

    @DeleteMapping("api/instructor/course/qna/comment")
    public ResponseEntity deleteCommentStudentInstructor(String id_comment, Principal principal) {
        return qnaService.deleteCommentStudent(id_comment, principal);
    }

    @PostMapping("api/instructor/course/qna/replies")
    public ResponseEntity postRepliesStudentInstructor(String id_comment, String body,
        Principal principal) {
        return qnaService.postRepliesStudent(id_comment, body, principal);
    }

    @PutMapping("api/instructor/course/qna/replies")
    public ResponseEntity updateRepliesStudentInstructor(String id_replies, String body,
        Principal principal) {
        return qnaService.updateRepliesStudent(id_replies, body, principal);
    }

    @DeleteMapping("api/instructor/course/qna/replies")
    public ResponseEntity deleteRepliesStudentInstructor(String id_replies, Principal principal) {
        return qnaService.deleteRepliesStudent(id_replies, principal);
    }

    @GetMapping("api/student/course/qna/post")
    public ResponseEntity getCoursePost(Integer page, String id_course, Principal principal) {
        return qnaService.getCoursePost(page, id_course, principal);
    }

    @GetMapping("api/student/course/qna/comment")
    public ResponseEntity getPostComment(Integer page, String id_post, Principal principal) {
        return qnaService.getPostComment(page, id_post, principal);
    }

    @GetMapping("api/student/course/qna/replies")
    public ResponseEntity getCommentReplies(Integer page, String id_comment, Principal principal) {
        return qnaService.getCommentReplies(page, id_comment, principal);
    }

    @GetMapping("api/instructor/course/qna/post")
    public ResponseEntity getCoursePostInstructor(Integer page, String id_course,
        Principal principal) {
        return qnaService.getCoursePost(page, id_course, principal);
    }

    @GetMapping("api/instructor/course/qna/comment")
    public ResponseEntity getPostCommentInstructor(Integer page, String id_post,
        Principal principal) {
        return qnaService.getPostComment(page, id_post, principal);
    }

    @GetMapping("api/instructor/course/qna/replies")
    public ResponseEntity getCommentRepliesInstructor(Integer page, String id_comment,
        Principal principal) {
        return qnaService.getCommentReplies(page, id_comment, principal);
    }
}
