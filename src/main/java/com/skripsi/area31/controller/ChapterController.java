package com.skripsi.area31.controller;

import com.skripsi.area31.model.course.Chapter;
import com.skripsi.area31.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin("**") @RestController @RequestMapping public class ChapterController {
    @Autowired ChapterService chapterService;

    /*Chapter*/
    @PostMapping("api/instructor/chapter/create") public ResponseEntity createChapter(
        @RequestBody Chapter chapter, Principal principal) {
        return chapterService.createChapter(chapter, principal);
    }

    @PutMapping("api/instructor/chapter/update")
    public ResponseEntity updateChapter(@RequestBody Chapter chapter, Principal principal) {
        return chapterService.updateChapter(chapter, principal);
    }

    @GetMapping("api/instructor/chapter")
    public ResponseEntity getAllChapterByCourse(Integer page, String id_course,
        Principal principal) {
        return chapterService.getListChapter(page, id_course, principal);
    }

    @DeleteMapping("api/instructor/chapter/delete")
    public ResponseEntity deleteChapter(String id_chapter) {
        return chapterService.deleteChapter(id_chapter);
    }

    @GetMapping("/api/student/chapter")
    public ResponseEntity getListChapter(Integer page, String id_course, Principal principal) {
        return chapterService.getListChapter(page, id_course, principal);
    }
}
