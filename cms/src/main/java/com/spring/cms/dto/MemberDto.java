package com.spring.cms.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.spring.cms.domain.Member;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class MemberDto {

    @ToString
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Join {

        @NotBlank(message = "이름을 입력해주세요")
        private String name;
        
        @NotBlank(message = "비밀번호를 입력해주세요")
        private String password;
        
        @NotBlank(message = "이메일을 입력해주세요")
        @Email(message = "이메일을 정확히 입력해주세요")
        private String email;

        @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
        private String hp;
        
        @NotNull(message = "권한을 선택해주세요")
        private Long authorityId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {
        @NotBlank(message = "이메일을 입력해주세요")
        @Email(message = "이메일을 정확히 입력해주세요")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요")
        private String password;

    }

    @Data
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String email;
        private String hp;
        private String createBy;
        private String lastModifiedBy;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public Response(Long id, String name, String email, String hp, String createBy, String lastModifiedBy, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.hp = hp;
            this.createBy = createBy;
            this.lastModifiedBy = lastModifiedBy;
            this.createdDate = createdDate;
            this.lastModifiedDate = lastModifiedDate;
        }
    }

    @Data
    @NoArgsConstructor
    public static class MemberResponse {
        private Long id;
        private String name;
        private String email;
        private String hp;
        private String createBy;
        private String lastModifiedBy;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
        private List<MemberAuthorityDto.Response> authorities;

        @QueryProjection
        public MemberResponse(Long id, String name, String email, String hp, String createBy, String lastModifiedBy, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.hp = hp;
            this.createBy = createBy;
            this.lastModifiedBy = lastModifiedBy;
            this.createdDate = createdDate;
            this.lastModifiedDate = lastModifiedDate;
        }
    }

    @Data
    public static class AuthResponse {
        private Long id;
        private String name;
        private String email;
    }

}
