package com.spring.cms.domain;

import com.spring.cms.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_SITE")
@Entity
public class Site extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SITE_ID")
    private Long id;

    @Column(name = "SITE_NAME", nullable = false)
    private String siteName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "USE_YN", length = 1, nullable = false)
    private Character useYn;
}
