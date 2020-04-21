package com.skripsi.area31.model.user;

import com.skripsi.area31.model.base.BaseModel;
import lombok.*;
import org.springframework.data.annotation.Id;

@EqualsAndHashCode(callSuper = true) @Data @Builder @AllArgsConstructor @NoArgsConstructor public class User extends BaseModel {
    @Id private String idUser;
    private String email;
    private String password;
    private String role;
}
