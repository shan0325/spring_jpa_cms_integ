package com.spring.cms.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import com.spring.cms.enums.MemberStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class MemberQueryDto {

    @Data
    @NoArgsConstructor
    public static class SearchMembersResponse {
        private Long id;
        private String name;
        private String email;
        private String hp;
        private MemberStatusEnum status;
        private String createBy;
        private String lastModifiedBy;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
        private List<MemberAuthorityQueryDto.Response> authorities;

        @QueryProjection
        public SearchMembersResponse(Long id, String name, String email, String hp, MemberStatusEnum status, String createBy, String lastModifiedBy, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.hp = hp;
            this.status = status;
            this.createBy = createBy;
            this.lastModifiedBy = lastModifiedBy;
            this.createdDate = createdDate;
            this.lastModifiedDate = lastModifiedDate;
        }
    }
}
