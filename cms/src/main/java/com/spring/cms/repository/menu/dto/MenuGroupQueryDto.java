package com.spring.cms.repository.menu.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MenuGroupQueryDto {

    @NoArgsConstructor
    @Data
    public static class MenuGroupsResponse {
        private Long id;
        private String groupName;
        private Long siteId;
        private String siteName;

        @QueryProjection
        public MenuGroupsResponse(Long id, String groupName, Long siteId, String siteName) {
            this.id = id;
            this.groupName = groupName;
            this.siteId = siteId;
            this.siteName = siteName;
        }
    }
}
