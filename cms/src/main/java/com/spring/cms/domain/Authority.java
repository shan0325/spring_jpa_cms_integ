package com.spring.cms.domain;

import com.spring.cms.domain.common.BaseEntity;
import com.spring.cms.enums.AuthorityTypeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Authority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Column(nullable = false)
    private String authority;

    @Column(nullable = false)
    private String authorityName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
