package com.spring.cms.domain.menu;

import com.spring.cms.domain.Site;

import javax.persistence.*;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_group_id")
    private Long id;

    @Column(nullable = false)
    private String groupName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;
}
