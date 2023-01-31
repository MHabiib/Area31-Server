package com.skripsi.area31.service.impl;

import com.skripsi.area31.model.course.*;
import com.skripsi.area31.model.user.User;
import com.skripsi.area31.repository.ComplaintRepository;
import com.skripsi.area31.repository.CourseRepository;
import com.skripsi.area31.repository.ReportRepository;
import com.skripsi.area31.repository.UserRepository;
import com.skripsi.area31.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service(value = "complaintService") public class ComplaintServiceImpl implements ComplaintService {
    @Autowired ComplaintRepository complaintRepository;
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired ReportRepository reportRepository;

    @Override public ResponseEntity getAllStudentComplaint(Principal principal, String id_course) {
        User user = userRepository.findByEmail(principal.getName());
        List<ComplaintResponse> complaintResponseList = new ArrayList<>();
        List<Complaint> complaintList =
            complaintRepository.findAllByIdUserAndIdCourse(user.getIdUser(), id_course);

        UserServiceImpl
            .setupComplainDetails(complaintResponseList, complaintList, reportRepository);
        return ResponseEntity.ok(complaintResponseList);
    }

    @Override
    public ResponseEntity getStudentComplaintDetails(Principal principal, String id_complaint) {
        Complaint complaint = complaintRepository.findComplaintByIdComplaint(id_complaint);
        return ResponseEntity.ok(complaint);
    }

    @Override public ResponseEntity getComplaintList(Integer page, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        PageRequest request = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Complaint> complaintPage =
            complaintRepository.findAllByIdInstructor(user.getIdUser(), request);

        return ResponseEntity.ok(toPageComplaintPageInstructor(complaintPage));
    }

    @Override public ResponseEntity getInstructorComplaintDetails(String id_complaint) {
        Complaint complaint = complaintRepository.findComplaintByIdComplaint(id_complaint);
        User user = userRepository.findByIdUser(complaint.getIdUser());
        Report report = reportRepository.findReportByIdReport(complaint.getIdReport());
        return ResponseEntity
            .ok(new ComplaintDetailsInstructor(complaint.getIdComplaint(), report.getIdReport(),
                user.getName(), complaint.getDescription(), report.getScore(),
                complaint.getCreatedAt(), complaint.getStatus()));
    }

    private Page<ComplaintPageInstructor> toPageComplaintPageInstructor(Page<Complaint> objects) {
        return objects.map(this::convertToPageComplaintPageInstructor);
    }

    private ComplaintPageInstructor convertToPageComplaintPageInstructor(Complaint complaint) {
        User complainer = userRepository.findByIdUser(complaint.getIdUser());
        Report report = reportRepository.findReportByIdReport(complaint.getIdReport());
        Course course = courseRepository.findCourseByIdCourse(report.getIdCourse());
        return new ComplaintPageInstructor(complaint.getIdComplaint(), complaint.getQuizTitle(),
            complainer.getName(), complaint.getDescription(), course.getName(), report.getScore(),
            complaint.getStatus());
    }
}
