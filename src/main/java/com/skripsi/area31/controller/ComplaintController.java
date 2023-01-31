package com.skripsi.area31.controller;

import com.skripsi.area31.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin("**") @RestController @RequestMapping public class ComplaintController {
    @Autowired ComplaintService complaintService;

    @GetMapping("api/student/complaint")
    public ResponseEntity getAllStudentComplaint(Principal principal, String id_course) {
        return complaintService.getAllStudentComplaint(principal, id_course);
    }

    @GetMapping("api/student/complaint/details")
    public ResponseEntity getStudentComplaintDetails(Principal principal, String id_complaint) {
        return complaintService.getStudentComplaintDetails(principal, id_complaint);
    }

    @GetMapping("api/instructor/complaint/details")
    public ResponseEntity getInstructorComplaintDetails(Principal principal, String id_complaint) {
        return complaintService.getInstructorComplaintDetails(id_complaint);
    }

    @PutMapping("api/instructor/complaint/update")
    public ResponseEntity updateComplaint(Principal principal, String id_complaint) {
        return complaintService.getInstructorComplaintDetails(id_complaint);
    }

    @GetMapping("/api/instructor/complaint")
    public ResponseEntity getComplaintList(Integer page, Principal principal) {
        return complaintService.getComplaintList(page, principal);
    }
}
