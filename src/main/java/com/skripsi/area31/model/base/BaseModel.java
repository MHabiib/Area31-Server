package com.skripsi.area31.model.base;

import com.skripsi.area31.util.FieldName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

@Data @NoArgsConstructor @AllArgsConstructor public class BaseModel {

    @Field(FieldName.BaseModel.VERSION) @Version private Long version;

}
