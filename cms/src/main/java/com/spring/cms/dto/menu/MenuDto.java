package com.spring.cms.dto.menu;

import com.spring.cms.annotation.Enum;
import com.spring.cms.domain.menu.Menu;
import com.spring.cms.enums.MenuType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MenuDto {

    @ToString
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
        @NotNull(message = "메뉴 그룹 아이디는 필수 값입니다.")
        private Long menuGroupId;

        private Long parentId;
        private Long topId;

        @NotNull(message = "레벨은 필수 값입니다.")
        private Integer level;

        private Integer ord;

        @NotBlank(message = "메뉴명은 필수 값입니다.")
        private String name;

        private String description;

        @NotNull(message = "사용유무는 필수 값입니다.")
        private Character useYn;

        @Enum(enumClass = MenuType.class)
        @NotBlank(message = "메뉴타입은 필수 값입니다.")
        private String menuType;

        private Long boardManagerId;
        private String link;
        private String linkTarget;
        private Long contentsId;
    }

    @Data
    public static class AllMenusResponse {
        private Long id;
        private Long menuGroupId;
        private Long parentId;
        private Long topId;
        private Integer level;
        private Integer ord;
        private String name;
        private List<AllMenusResponse> childMenus;

        public AllMenusResponse(Menu menu) {
            this.id = menu.getId();
            this.menuGroupId = menu.getMenuGroup().getId();
            this.parentId = menu.getParent() != null ? menu.getParent().getId() : null;
            this.topId = menu.getTop() != null ? menu.getTop().getId() : null;
            this.level = menu.getLevel();
            this.ord = menu.getOrd();
            this.name = menu.getName();
            this.childMenus = menu.getChild().stream()
                    .map(AllMenusResponse::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    public static class MenuDetailResponse {
        private Long id;
        private Long menuGroupId;
        private Long parentId;
        private Long topId;
        private Integer level;
        private Integer ord;
        private String name;
        private String description;
        private Character useYn;
        private MenuType menuType;
        private Long boardManagerId;
        private String link;
        private String linkTarget;
        private Long contentsId;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
    }

    @ToString
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        @NotNull(message = "메뉴 그룹 아이디는 필수 값입니다.")
        private Long menuGroupId;

        @NotBlank(message = "메뉴명은 필수 값입니다.")
        private String name;

        private String description;

        @NotNull(message = "사용유무는 필수 값입니다.")
        private Character useYn;

        @Enum(enumClass = MenuType.class)
        @NotBlank(message = "메뉴타입은 필수 값입니다.")
        private String menuType;

        private Long boardManagerId;
        private String link;
        private String linkTarget;
        private Long contentsId;
    }
}
