package com.spring.cms.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class VersionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_info_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private Long version;

    public static VersionInfo createEmptyVersionInfo() {
        return new VersionInfo();
    }
}
