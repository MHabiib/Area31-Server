package com.skripsi.area31.repository;

import com.skripsi.area31.model.course.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface CourseRepository extends MongoRepository<Course, String> {
    Integer countCourseByIdInstructor(String idInstructor);

    Course findCourseByCourseId(String courseId);

    Course findCourseByIdCourse(String idCourse);

    List<Course> findCourseByIdInstructor(String idInstructor);

    List<Course> findTop5ByIdInstructorAndStatus(String idInstructor, String status);

    Page<Course> findAllByIdStudent(String idStudent, Pageable pageable);
}
