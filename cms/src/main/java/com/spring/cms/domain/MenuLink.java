package com.spring.cms.domain;

import com.spring.cms.enums.MenuLinkTargetEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class MenuLink {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_link_id")
    private Long id;

    @Column(nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuLinkTargetEnum linkTarget;

    @Builder(access = AccessLevel.PRIVATE)
    public MenuLink(String link, MenuLinkTargetEnum linkTarget) {
        this.link = link;
        this.linkTarget = linkTarget;
    }

    //==생성 메서드==//
    public static MenuLink createMenuLink(String link, MenuLinkTargetEnum linkTarget) {
        return MenuLink.builder()
                .link(link)
                .linkTarget(linkTarget)
                .build();
    }
}
