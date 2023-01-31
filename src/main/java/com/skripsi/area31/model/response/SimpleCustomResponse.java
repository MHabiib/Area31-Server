package com.skripsi.area31.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class SimpleCustomResponse {
    private Integer code;
    private String message;
}
