package com.skripsi.area31.model.user;

import com.skripsi.area31.model.base.BaseModel;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Set;

@EqualsAndHashCode(callSuper = true) @Data @Builder @AllArgsConstructor @NoArgsConstructor
public class User extends BaseModel {
    @Id private String idUser;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String role;
    private Set<String> idCourse;
    private Integer resetCode;
}
