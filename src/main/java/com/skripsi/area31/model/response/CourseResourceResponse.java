package com.skripsi.area31.model.response;

import com.skripsi.area31.model.course.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class CourseResourceResponse {
    private Integer code;
    private String message;
    private List<Resource> resourceList;
}
