package com.shan.spring.cms.dto;

import com.shan.spring.cms.enums.AuthorityTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthorityDto {

    @NoArgsConstructor
    @Data
    public static class Response {
        private Long id;
        private String authority;
        private String authorityName;
        private AuthorityTypeEnum authorityType;

        public Response(Long id, String authority, String authorityName, AuthorityTypeEnum authorityType) {
            this.id = id;
            this.authority = authority;
            this.authorityName = authorityName;
            this.authorityType = authorityType;
        }
    }
}
