package com.skripsi.area31.service.impl;

import com.skripsi.area31.model.course.Chapter;
import com.skripsi.area31.model.course.Course;
import com.skripsi.area31.model.response.SimpleCustomResponse;
import com.skripsi.area31.repository.ChapterRepository;
import com.skripsi.area31.repository.CourseRepository;
import com.skripsi.area31.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Calendar;

@Service(value = "chapterService") public class ChapterServiceImpl implements ChapterService {
    @Autowired ChapterRepository chapterRepository;
    @Autowired CourseRepository courseRepository;

    @Override public ResponseEntity getAllChapterByCourse(String idCourse) {
        return new ResponseEntity<>(chapterRepository.findChapterByIdCourse(idCourse),
            HttpStatus.OK);
    }

    @Override
    public ResponseEntity getListChapter(Integer page, String id_course, Principal principal) {
        Course course = courseRepository.findCourseByIdCourse(id_course);
        PageRequest request = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
        return ResponseEntity
            .ok(chapterRepository.findChapterByIdCourse(course.getIdCourse(), request));
    }

    @Override public ResponseEntity createChapter(Chapter chapter, Principal principal) {
        if (!courseRepository.findById(chapter.getIdCourse()).isPresent()) {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Course not found"),
                HttpStatus.BAD_REQUEST);
        }
        chapter.setCreatedDate(Calendar.getInstance().getTimeInMillis());
        chapterRepository.save(chapter);
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Success create chapter"),
            HttpStatus.OK);
    }

    @Override public ResponseEntity updateChapter(Chapter chapter, Principal principal) {
        Chapter chapterExist = chapterRepository.findChapterByIdChapter(chapter.getIdChapter());
        chapterExist.setTitle(chapter.getTitle());
        chapterExist.setDescription(chapter.getDescription());
        chapterExist.setLectureNote(chapter.getLectureNote());
        chapterRepository.save(chapterExist);
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Success update chapter"),
            HttpStatus.OK);
    }

    @Override public ResponseEntity deleteChapter(String id_chapter) {
        Chapter chapter = chapterRepository.findChapterByIdChapter(id_chapter);
        chapterRepository.delete(chapter);
        return ResponseEntity.ok("Chapter deleted");
    }
}
