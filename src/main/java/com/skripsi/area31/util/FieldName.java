package com.skripsi.area31.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public interface FieldName {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    abstract class BaseModel {

        public static final String ID = "id";

        public static final String CREATED_AT = "createdAt";

        public static final String CREATED_BY = "createdBy";

        public static final String UPDATED_AT = "updatedAt";

        public static final String UPDATED_BY = "updatedBy";

        public static final String DELETED = "deleted";

        public static final String VERSION = "version";

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    abstract class Constants {

        public static final String CLIENT_ID = "area31-client";

        public static final String CLIENT_SECRET = "area31-secret";

        public static final String GRANT_TYPE_PASSWORD = "password";

        public static final String AUTHORIZATION_CODE = "authorization_code";

        public static final String REFRESH_TOKEN = "refresh_token";

        public static final String IMPLICIT = "implicit";

        public static final String SCOPE_READ = "read";

        public static final String SCOPE_WRITE = "write";

        public static final String TRUST = "trust";

        public static final Integer ACCESS_TOKEN_VALIDITY_SECONDS = 24 * 60 * 60;

        public static final Integer REFRESH_TOKEN_VALIDITY_SECONDS = 30 * 24 * 60 * 60;

        public static final String MD_ALGORITHM_NOT_AVAILABLE =
            "MD5 algorithm not available.  Fatal (should be in the JDK).";

        public static final String ENCODING_NO_AVAILABLE =
            "UTF-8 encoding not available.  Fatal (should be in the JDK).";

        public static final String RESOURCE_ID = "resource_id";
    }
}
