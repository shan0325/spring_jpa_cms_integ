package com.spring.cms.domain.menu;

import com.spring.cms.domain.Contents;
import com.spring.cms.domain.board.BoardManager;
import com.spring.cms.domain.common.BaseEntity;
import com.spring.cms.dto.MenuDto;
import com.spring.cms.enums.MenuTypeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_MENU")
@Entity
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_GROUP_ID")
    private MenuGroup menuGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Menu parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("ord asc, createdDate desc")
    private List<Menu> child = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOP_ID")
    private Menu top;

    @Column(name = "LEVEL", nullable = false)
    private Integer level;

    @Column(name = "ORD")
    private Integer ord;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "USE_YN", length = 1, nullable = false)
    private Character useYn;

    @Enumerated(EnumType.STRING)
    @Column(name = "MENU_TYPE", nullable = false)
    private MenuTypeEnum menuType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_MANAGER_ID")
    private BoardManager boardManager;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "MENU_LINK_ID")
    private MenuLink menuLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTENTS_ID")
    private Contents contents;

    @Column(name = "MATERIAL_ICON", length = 100)
    private String materialIcon;

    @Column(name = "VIEW_PATH")
    private String viewPath;

    @Builder(access = AccessLevel.PRIVATE)
    public Menu(MenuGroup menuGroup, Menu top, Integer level, Integer ord, String name, String description, Character useYn,
                MenuTypeEnum menuType, BoardManager boardManager, MenuLink menuLink, Contents contents, String materialIcon, String viewPath) {
        this.menuGroup = menuGroup;
        this.top = top;
        this.level = level;
        this.ord = ord;
        this.name = name;
        this.description = description;
        this.useYn = useYn;
        this.menuType = menuType;
        this.boardManager = boardManager;
        this.menuLink = menuLink;
        this.contents = contents;
        this.materialIcon = materialIcon;
        this.viewPath = viewPath;
    }

    //==연관관계 메서드==//
    public void addParentMenu(Menu parent) {
        this.parent = parent;
        if (parent != null) {
            parent.getChild().add(this);
        }
    }

    //==생성 메서드==//
    public static Menu createMenu(MenuDto.Create create, MenuGroup menuGroup, Menu parent, Menu top, BoardManager boardManager,
                                  MenuLink menuLink, Contents contents) {
        Menu menu = Menu.builder()
                .menuGroup(menuGroup)
                .top(top)
                .level(create.getLevel())
                .ord(create.getOrd())
                .name(create.getName())
                .description(create.getDescription())
                .useYn(create.getUseYn())
                .menuType(create.getMenuType())
                .boardManager(boardManager)
                .menuLink(menuLink)
                .contents(contents)
                .materialIcon(create.getMaterialIcon())
                .viewPath(create.getViewPath())
                .build();

        menu.addParentMenu(parent);
        return menu;
    }

    //==수정 메서드==//
    public void updateMenu(MenuDto.Update update, MenuGroup menuGroup, BoardManager boardManager, MenuLink menuLink, Contents contents) {
        this.menuGroup = menuGroup;
        this.ord = update.getOrd();
        this.name = update.getName();
        this.description = update.getDescription();
        this.useYn = update.getUseYn();
        this.menuType = update.getMenuType();
        this.boardManager = boardManager;
        this.menuLink = menuLink;
        this.contents = contents;
        this.materialIcon = update.getMaterialIcon();
        this.viewPath = update.getViewPath();
    }
}
