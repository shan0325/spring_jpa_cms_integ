package com.shan.spring.cms.repository.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shan.spring.cms.enums.AuthorityTypeEnum;
import lombok.Getter;

public class MemberAuthorityQueryDto {

    @Getter
    public static class Response {
        private Long memberId;
        private Long authorityId;
        private String authority;
        private String authorityName;
        private AuthorityTypeEnum authorityType;

        @QueryProjection
        public Response(Long memberId, Long authorityId, String authority, String authorityName, AuthorityTypeEnum authorityType) {
            this.memberId = memberId;
            this.authorityId = authorityId;
            this.authority = authority;
            this.authorityName = authorityName;
            this.authorityType = authorityType;
        }
    }
}
