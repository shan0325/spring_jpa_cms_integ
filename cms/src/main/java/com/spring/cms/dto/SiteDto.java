package com.spring.cms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public class SiteDto {

    @NoArgsConstructor
    @Data
    public static class SiteDetailResponse {
        private Long id;
        private String siteName;
        private String description;

        public SiteDetailResponse(Long id, String siteName, String description) {
            this.id = id;
            this.siteName = siteName;
            this.description = description;
        }
    }
}
