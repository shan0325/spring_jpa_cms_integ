package com.spring.cms.dto;

import lombok.Data;

public class SiteDto {

    @Data
    public static class SiteDetailResponse {
        private Long id;
        private String siteName;
        private String description;
    }
}
