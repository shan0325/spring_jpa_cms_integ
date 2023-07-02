package com.spring.cms.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_VERSION_INFO")
@Entity
public class VersionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VERSION_INFO_ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "VERSION", nullable = false)
    private Long version;

    public static VersionInfo createEmptyVersionInfo() {
        return new VersionInfo();
    }
}
