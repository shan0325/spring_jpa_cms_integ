package com.spring.cms.domain.security;

import com.spring.cms.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_SECURED_RESOURCE")
@Entity
public class SecuredResource extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESOURCE_ID")
    private Long id;

    @Column(name = "RESOURCE_NAME", nullable = false)
    private String resourceName;

    @Column(name = "RESOURCE_PATTERN", nullable = false)
    private String resourcePattern;

    @Column(name = "RESOURCE_TYPE", length = 10, nullable = false)
    private String resourceType; // 리소스 타입(URL, METHOD)

    @Column(name = "ORD")
    private Integer ord;
}
