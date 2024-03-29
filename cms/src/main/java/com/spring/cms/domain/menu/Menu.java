package com.spring.cms.domain.menu;

import com.spring.cms.domain.Contents;
import com.spring.cms.domain.board.BoardManager;
import com.spring.cms.domain.common.BaseEntity;
import com.spring.cms.dto.menu.MenuDto;
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
@Entity
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("ord asc, createdDate desc")
    private List<Menu> child = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top_id")
    private Menu top;

    @Column(nullable = false)
    private Integer level;

    private Integer ord;

    @Column(length = 100, nullable = false)
    private String name;

    private String description;

    @Column(length = 1, nullable = false)
    private Character useYn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuTypeEnum menuType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_manager_id")
    private BoardManager boardManager;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "menu_link_id")
    private MenuLink menuLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contents_id")
    private Contents contents;

    @Column(length = 100)
    private String materialIcon;
    
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
