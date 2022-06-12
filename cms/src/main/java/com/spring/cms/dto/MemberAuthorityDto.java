package com.spring.cms.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.spring.cms.enums.AuthorityType;
import lombok.Getter;

public class MemberAuthorityDto {

    @Getter
    public static class Response {
        private Long memberId;
        private Long authorityId;
        private String authority;
        private String authorityName;
        private AuthorityType authorityType;

        @QueryProjection
        public Response(Long memberId, Long authorityId, String authority, String authorityName, AuthorityType authorityType) {
            this.memberId = memberId;
            this.authorityId = authorityId;
            this.authority = authority;
            this.authorityName = authorityName;
            this.authorityType = authorityType;
        }
    }
}
