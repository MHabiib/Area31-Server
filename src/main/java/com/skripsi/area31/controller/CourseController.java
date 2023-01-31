package com.skripsi.area31.controller;

import com.skripsi.area31.model.course.Course;
import com.skripsi.area31.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@CrossOrigin("**") @RestController @RequestMapping public class CourseController {
    @Autowired CourseService courseService;

    @GetMapping("/api/student/course")
    public ResponseEntity loadListCourse(Integer page, Principal principal) {
        return courseService.loadListCourse(page, principal);
    }

    @GetMapping("/api/student/course/check")
    public ResponseEntity checkCourse(String courseId, Principal principal) {
        return courseService.checkCourse(courseId, principal);
    }

    @PutMapping("/api/student/course/joinStepOne")
    public ResponseEntity joinCourse(String courseId, String password, Principal principal) {
        return courseService.joinCourse(courseId, password, principal);
    }

    @GetMapping("/api/student/course/details")
    public ResponseEntity courseDetails(String idCourse, Principal principal) {
        return courseService.courseDetails(idCourse, principal);
    }

    @PutMapping("/api/student/course/leave")
    public ResponseEntity leaveCourse(String idCourse, Principal principal) {
        return courseService.leaveCourse(idCourse, principal);
    }

    @GetMapping("/api/student/course/resource")
    public ResponseEntity courseResource(String idCourse, Principal principal) {
        return courseService.courseResource(idCourse, principal);
    }

    @PostMapping("api/instructor/course/create")
    public ResponseEntity createCourse(@RequestBody Course course, Principal principal)
        throws IOException {
        return courseService.createCourse(course, principal);
    }

    @PutMapping("api/instructor/course/create")
    public ResponseEntity updateCourse(@RequestBody Course course, Principal principal)
        throws IOException {
        return courseService.updateCourse(course, principal);
    }

    @PutMapping("api/instructor/course/archive")
    public ResponseEntity archiveCourse(String id_course, Principal principal) {
        return courseService.archiveCourse(id_course, principal);
    }

    @GetMapping("api/instructor/course") public ResponseEntity getAllCourse(Principal principal) {
        return courseService.getAllCourse(principal);
    }

    @GetMapping("/api/instructor/course/details")
    public ResponseEntity courseDetailsInstructor(String idCourse, Principal principal) {
        return courseService.courseDetailsInstructor(idCourse, principal);
    }

    @PostMapping("api/instructor/course/resource")
    public ResponseEntity uploadResource(Principal principal,
        @RequestPart("file") MultipartFile file, String idCourse) {
        return courseService.uploadResource(principal, file, idCourse);
    }

    @GetMapping("/api/instructor/course/resource")
    public ResponseEntity courseResourceInstructor(String idCourse, Principal principal) {
        return courseService.courseResource(idCourse, principal);
    }

    @GetMapping("/api/instructor/course/members")
    public ResponseEntity courseMembers(String idCourse, Principal principal) {
        return courseService.courseMembers(idCourse, principal);
    }

    @PutMapping("/api/instructor/course/delete-member")
    public ResponseEntity deleteMember(String idUser, String idCourse) {
        return courseService.deleteMember(idUser, idCourse);
    }

    @DeleteMapping("/api/instructor/course/resource")
    public ResponseEntity deleteResource(String fileName, String idCourse) {
        return courseService.deleteResource(fileName, idCourse);
    }

    @PostMapping("upload/file")
    public ResponseEntity uploadFile(@RequestPart("file") MultipartFile file) {
        return courseService.uploadFile(file);
    }
}
