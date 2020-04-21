package com.skripsi.area31.model.base;

import com.skripsi.area31.util.FieldName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseModel {

    @Field(FieldName.BaseModel.CREATED_AT)
    @CreatedDate
    private Long createdAt;

    @Field(FieldName.BaseModel.CREATED_BY)
    @CreatedBy
    private String createdBy;

    @Field(FieldName.BaseModel.UPDATED_AT)
    @LastModifiedDate
    private Long updatedAt;

    @Field(FieldName.BaseModel.UPDATED_BY)
    @LastModifiedBy
    private String updatedBy;

    @Field(FieldName.BaseModel.DELETED)
    private boolean deleted;

    @Field(FieldName.BaseModel.VERSION)
    @Version
    private Long version;

}
