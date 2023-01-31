package com.skripsi.area31.service;

import com.skripsi.area31.model.course.Course;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface CourseService {
    ResponseEntity createCourse(Course course, Principal principal) throws IOException;

    ResponseEntity updateCourse(Course course, Principal principal) throws IOException;

    ResponseEntity archiveCourse(String id_course, Principal principal);

    ResponseEntity getAllCourse(Principal principal);

    ResponseEntity loadListCourse(Integer page, Principal principal);

    ResponseEntity joinCourse(String courseId, String password, Principal principal);

    ResponseEntity checkCourse(String courseId, Principal principal);

    ResponseEntity courseDetails(String idCourse, Principal principal);

    ResponseEntity courseDetailsInstructor(String idCourse, Principal principal);

    ResponseEntity leaveCourse(String idCourse, Principal principal);

    ResponseEntity uploadResource(Principal principal, MultipartFile file, String idCourse);

    ResponseEntity courseResource(String idCourse, Principal principal);

    ResponseEntity uploadFile(MultipartFile file);

    ResponseEntity courseMembers(String idCourse, Principal principal);

    ResponseEntity deleteMember(String idUser, String idCourse);

    ResponseEntity deleteResource(String fileName, String idCourse);
}
