package com.skripsi.area31.model.response;

import com.skripsi.area31.model.course.CourseDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class CourseDetailsResponse {
    private Integer code;
    private CourseDetails courseDetails;
}
