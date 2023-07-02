package com.shan.spring.cms.repository.code.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

public class CodeQueryDto {

    @Data
    public static class AllCodesResponseQuery {
        private Long id;
        private Long parentId;
        private Long topId;
        private String code;
        private String name;
        private String description;
        private Integer level;
        private Integer ord;
        private Character useYn;
        private List<AllCodesResponseQuery> childCodes;

        @QueryProjection
        public AllCodesResponseQuery(Long id, Long parentId, Long topId, String code, String name, String description, Integer level, Integer ord, Character useYn) {
            this.id = id;
            this.parentId = parentId;
            this.topId = topId;
            this.code = code;
            this.name = name;
            this.description = description;
            this.level = level;
            this.ord = ord;
            this.useYn = useYn;
        }
    }
}
