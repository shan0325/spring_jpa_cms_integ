package com.spring.cms.domain;

import javax.persistence.*;

@Entity
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id")
    private Long id;

    @Column(nullable = false)
    private String siteName;

    private String description;
}
