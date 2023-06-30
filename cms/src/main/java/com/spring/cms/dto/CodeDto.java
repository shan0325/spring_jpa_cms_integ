package com.spring.cms.dto;

import com.spring.cms.domain.Code;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CodeDto {

    @ToString
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
        private Long parentId;
        private Long topId;

        @NotBlank(message = "코드를 입력해주세요")
        private String code;

        @NotBlank(message = "코드명을 입력해주세요")
        private String name;

        private String description;

        @NotNull(message = "순서를 입력해주세요")
        private Integer ord;

        @NotNull(message = "사용유무를 입력해주세요")
        private Character useYn;
    }

    @Data
    public static class AllCodesResponse {
        private Long id;
        private Long parentId;
        private Long topId;
        private String code;
        private String name;
        private String description;
        private Integer level;
        private Integer ord;
        private Character useYn;
        private List<AllCodesResponse> childCodes;

        public AllCodesResponse(Code code) {
            this.id = code.getId();
            this.parentId = code.getParent() != null ? code.getParent().getId() : null;
            this.topId = code.getTop() != null ? code.getTop().getId() : null;
            this.code = code.getCode();
            this.name = code.getName();
            this.description = code.getDescription();
            this.level = code.getLevel();
            this.ord = code.getOrd();
            this.useYn = code.getUseYn();
            this.childCodes = code.getChilds().stream()
                    .map(AllCodesResponse::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    public static class CodeResponse {
        private Long id;
        private Long parentId;
        private Long topId;
        private String code;
        private String name;
        private String description;
        private Integer level;
        private Integer ord;
        private Character useYn;
    }

    @ToString
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        @NotBlank(message = "코드명을 입력해주세요")
        private String name;

        private String description;

        @NotNull(message = "순서를 입력해주세요")
        private Integer ord;

        @NotNull(message = "사용유무를 입력해주세요")
        private Character useYn;
    }
}
