package com.shan.spring.cms.repository.menu.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shan.spring.cms.enums.MenuTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class MenuQueryDto {

    @Data
    public static class CreateResponseQuery {
        private Long id;
        private Long parentId;
        private Long topId;
        private Integer level;
        private Integer ord;
        private String name;
        private String description;
        private Character useYn;
        private MenuTypeEnum menuType;
        private Long menuTypeRefId;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        @QueryProjection
        public CreateResponseQuery(Long id, Long parentId, Long topId, Integer level, Integer ord, String name, String description, Character useYn, MenuTypeEnum menuType, Long menuTypeRefId, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
            this.id = id;
            this.parentId = parentId;
            this.topId = topId;
            this.level = level;
            this.ord = ord;
            this.name = name;
            this.description = description;
            this.useYn = useYn;
            this.menuType = menuType;
            this.menuTypeRefId = menuTypeRefId;
            this.createdDate = createdDate;
            this.lastModifiedDate = lastModifiedDate;
        }
    }

    @Data
    public static class AllMenusResponseQuery {
        private Long id;
        private Long menuGroupId;
        private Long parentId;
        private Long topId;
        private Integer level;
        private Integer ord;
        private String name;
        private List<AllMenusResponseQuery> childMenus;

        @QueryProjection
        public AllMenusResponseQuery(Long id, Long menuGroupId, Long parentId, Long topId, Integer level, Integer ord, String name) {
            this.id = id;
            this.menuGroupId = menuGroupId;
            this.parentId = parentId;
            this.topId = topId;
            this.level = level;
            this.ord = ord;
            this.name = name;
        }
    }

    @Data
    public static class NaviMenusResponseQuery {
        private Long id;
        private Long menuGroupId;
        private Long parentId;
        private Long topId;
        private Integer level;
        private Integer ord;
        private String name;
        private MenuTypeEnum menuType;
        private String materialIcon;
        private String viewPath;
        private List<NaviMenusResponseQuery> childMenus;

        @QueryProjection
        public NaviMenusResponseQuery(Long id, Long menuGroupId, Long parentId, Long topId, Integer level, Integer ord,
                                      String name, MenuTypeEnum menuType, String materialIcon, String viewPath) {
            this.id = id;
            this.menuGroupId = menuGroupId;
            this.parentId = parentId;
            this.topId = topId;
            this.level = level;
            this.ord = ord;
            this.name = name;
            this.menuType = menuType;
            this.materialIcon = materialIcon;
            this.viewPath = viewPath;
        }
    }
}
