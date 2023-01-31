package com.skripsi.area31.service;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface QnaService {
    ResponseEntity postQna(String id_course, String title, String body, Principal principal);

    ResponseEntity updatePostQna(String id_post, String title, String body);

    ResponseEntity deletePostQna(String id_post);

    ResponseEntity postCommentStudent(String id_post, String body, Principal principal);

    ResponseEntity postRepliesStudent(String id_comment, String body, Principal principal);

    ResponseEntity getCoursePost(Integer page, String id_course, Principal principal);

    ResponseEntity getPostComment(Integer page, String id_post, Principal principal);

    ResponseEntity getCommentReplies(Integer page, String id_comment, Principal principal);

    ResponseEntity deleteCommentStudent(String id_comment, Principal principal);

    ResponseEntity deleteRepliesStudent(String id_replies, Principal principal);

    ResponseEntity updateCommentStudent(String id_comment, String body, Principal principal);

    ResponseEntity updateRepliesStudent(String id_replies, String body, Principal principal);
}
