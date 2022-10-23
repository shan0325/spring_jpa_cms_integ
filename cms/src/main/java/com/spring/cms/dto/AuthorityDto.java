package com.spring.cms.dto;

import com.spring.cms.enums.AuthorityType;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthorityDto {

    @NoArgsConstructor
    @Data
    public static class Response {
        private Long id;
        private String authority;
        private String authorityName;
        private AuthorityType authorityType;

        public Response(Long id, String authority, String authorityName, AuthorityType authorityType) {
            this.id = id;
            this.authority = authority;
            this.authorityName = authorityName;
            this.authorityType = authorityType;
        }
    }
}
