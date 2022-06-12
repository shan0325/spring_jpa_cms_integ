package com.spring.cms.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.spring.cms.domain.Authority;
import com.spring.cms.domain.MemberAuthority;
import com.spring.cms.enums.AuthorityType;
import lombok.Getter;

public class AuthorityDto {

    @Getter
    public static class Response {
        private Long id;
        private String authority;
        private String authorityName;
        private AuthorityType authorityType;

        @QueryProjection
        public Response(Long id, String authority, String authorityName, AuthorityType authorityType) {
            this.id = id;
            this.authority = authority;
            this.authorityName = authorityName;
            this.authorityType = authorityType;
        }
    }
}
