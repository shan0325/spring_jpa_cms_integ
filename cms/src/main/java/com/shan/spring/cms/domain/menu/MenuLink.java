package com.shan.spring.cms.domain.menu;

import com.shan.spring.cms.enums.MenuLinkTargetEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Table(name = "TB_MENU_LINK")
@Entity
public class MenuLink {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_LINK_ID")
    private Long id;

    @Column(name = "LINK", nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(name = "LINK_TARGET", nullable = false)
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
