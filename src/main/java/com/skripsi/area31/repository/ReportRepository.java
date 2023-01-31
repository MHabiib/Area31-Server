package com.skripsi.area31.repository;

import com.skripsi.area31.model.course.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface ReportRepository extends MongoRepository<Report, String> {
    List<Report> findReportByIdStudentAndIdCourseAndAssignedAtIsNull(String idStudent,
        String idCourse);

    Page<Report> findReportByIdStudentAndIdCourse(String idStudent, String idCourse,
        Pageable pageable);

    Page<Report> findAllByIdCourse(String idCourse, Pageable pageable);

    List<Report> findAllByIdQuizOrderByQuizDateDesc(String idQuiz);

    Report findReportByIdQuizAndIdStudent(String idQuiz, String idStudent);

    Report findReportByIdReport(String idReport);
}
