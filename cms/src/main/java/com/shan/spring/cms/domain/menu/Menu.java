package com.shan.spring.cms.domain.menu;

import com.shan.spring.cms.domain.common.BaseEntity;
import com.shan.spring.cms.dto.MenuDto;
import com.shan.spring.cms.enums.MenuTypeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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

    @Column(name = "MENU_TYPE_REF_ID")
    private Long menuTypeRefId;

    @Column(name = "MATERIAL_ICON", length = 100)
    private String materialIcon;

    @Comment("프론트 엔드 뷰 이동 페이지")
    @Column(name = "VIEW_PATH")
    private String viewPath;

    @Builder(access = AccessLevel.PRIVATE)
    public Menu(MenuGroup menuGroup, Menu top, Integer level, Integer ord, String name, String description, Character useYn,
                MenuTypeEnum menuType, Long menuTypeRefId, String materialIcon, String viewPath) {
        this.menuGroup = menuGroup;
        this.top = top;
        this.level = level;
        this.ord = ord;
        this.name = name;
        this.description = description;
        this.useYn = useYn;
        this.menuType = menuType;
        this.menuTypeRefId = menuTypeRefId;
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
    public static Menu createMenu(MenuDto.Create create, MenuGroup menuGroup, Menu parent, Menu top, Long menuTypeRefId) {
        Menu menu = Menu.builder()
                .menuGroup(menuGroup)
                .top(top)
                .level(create.getLevel())
                .ord(create.getOrd())
                .name(create.getName())
                .description(create.getDescription())
                .useYn(create.getUseYn())
                .menuType(create.getMenuType())
                .menuTypeRefId(menuTypeRefId)
                .materialIcon(create.getMaterialIcon())
                .viewPath(create.getViewPath())
                .build();

        menu.addParentMenu(parent);
        return menu;
    }

    //==수정 메서드==//
    public void updateMenu(MenuDto.Update update, MenuGroup menuGroup, Long menuTypeRefId) {
        this.menuGroup = menuGroup;
        this.ord = update.getOrd();
        this.name = update.getName();
        this.description = update.getDescription();
        this.useYn = update.getUseYn();
        this.menuType = update.getMenuType();
        this.menuTypeRefId = menuTypeRefId;
        this.materialIcon = update.getMaterialIcon();
        this.viewPath = update.getViewPath();
    }
}
