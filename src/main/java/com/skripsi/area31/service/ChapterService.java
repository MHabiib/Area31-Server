package com.skripsi.area31.service;

import com.skripsi.area31.model.course.Chapter;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface ChapterService {
    ResponseEntity createChapter(Chapter chapter, Principal principal);

    ResponseEntity updateChapter(Chapter chapter, Principal principal);

    ResponseEntity deleteChapter(String id_chapter);

    ResponseEntity getAllChapterByCourse(String idCourse);

    ResponseEntity getListChapter(Integer page, String id_course, Principal principal);
}
