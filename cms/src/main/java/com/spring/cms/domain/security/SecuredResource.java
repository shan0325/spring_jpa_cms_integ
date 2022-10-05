package com.spring.cms.domain.security;

import com.spring.cms.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class SecuredResource extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long id;

    @Column(nullable = false)
    private String resourceName;

    @Column(nullable = false)
    private String resourcePattern;

    @Column(length = 10, nullable = false)
    private String resourceType; // 리소스 타입(URL, METHOD)

    private Integer ord;
}
