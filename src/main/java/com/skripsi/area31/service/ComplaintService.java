package com.skripsi.area31.service;

import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface ComplaintService {
    ResponseEntity getAllStudentComplaint(Principal principal, String id_course);

    ResponseEntity getStudentComplaintDetails(Principal principal, String id_complaint);

    ResponseEntity getComplaintList(Integer page, Principal principal);

    ResponseEntity getInstructorComplaintDetails(String id_complaint);
}
