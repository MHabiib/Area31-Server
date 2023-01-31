package com.skripsi.area31.model.response;

import com.skripsi.area31.model.course.ComplaintResponseDashboard;
import com.skripsi.area31.model.course.Course;
import com.skripsi.area31.model.course.QuestionBank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class InstructorDashboardResponse {
    private List<Course> courseList;
    private List<QuestionBank> questionBankList;
    private List<ComplaintResponseDashboard> complaintList;
}
