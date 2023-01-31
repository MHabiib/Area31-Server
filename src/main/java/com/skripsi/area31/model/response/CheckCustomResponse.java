package com.skripsi.area31.model.response;

import com.skripsi.area31.model.course.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class CheckCustomResponse {
    private Integer code;
    private String message;
    private Course course;

    public CheckCustomResponse(int code, Course course) {
        this.code = code;
        this.course = course;
    }

    public CheckCustomResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
