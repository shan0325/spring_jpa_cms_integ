package com.shan.spring.cms.dto;


import lombok.Data;

public class VersionInfoDto {

    @Data
    public static class VersionInfoResponse {
        private Long id;
        private String name;
        private Long version;
    }
}
