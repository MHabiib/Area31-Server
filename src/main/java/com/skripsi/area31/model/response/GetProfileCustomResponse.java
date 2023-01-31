package com.skripsi.area31.model.response;

import com.skripsi.area31.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class GetProfileCustomResponse {
    private Integer code;
    private String message;
    private User user;

    public GetProfileCustomResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public GetProfileCustomResponse(int code, User user) {
        this.code = code;
        this.user = user;
    }
}
