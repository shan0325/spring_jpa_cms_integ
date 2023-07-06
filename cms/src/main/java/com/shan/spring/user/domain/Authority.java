package com.shan.spring.user.domain;

import com.shan.spring.user.domain.common.BaseEntity;
import com.shan.spring.user.enums.AuthorityTypeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_AUTHORITY")
@Entity
public class Authority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_ID")
    private Long id;

    @Column(name = "AUTHORITY", nullable = false)
    private String authority;

    @Column(name = "AUTHORITY_NAME", nullable = false)
    private String authorityName;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTHORITY_TYPE", nullable = false)
    private AuthorityTypeEnum authorityType;

    @Builder(access = AccessLevel.PRIVATE)
    public Authority(String authority, String authorityName, AuthorityTypeEnum authorityType) {
        this.authority = authority;
        this.authorityName = authorityName;
        this.authorityType = authorityType;
    }

    //==생성 메서드==//
    public static Authority createAuthority(String authority, String authorityName, AuthorityTypeEnum authorityType) {
        return Authority.builder()
                .authority(authority)
                .authorityName(authorityName)
                .authorityType(authorityType)
                .build();
    }

}