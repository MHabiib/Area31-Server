package com.skripsi.area31.repository;

import com.skripsi.area31.model.course.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface ChapterRepository extends MongoRepository<Chapter, String> {
    List<Chapter> findChapterByIdCourse(String idCourse);

    Page<Chapter> findChapterByIdCourse(String idCourse, Pageable pageable);

    Chapter findChapterByIdChapter(String idChapter);
}
