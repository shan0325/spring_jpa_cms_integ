package com.spring.cms.repository.menu;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.cms.dto.menu.MenuQueryDto;
import com.spring.cms.dto.menu.QMenuQueryDto_AllMenusResponseQuery;
import com.spring.cms.dto.menu.QMenuQueryDto_CreateResponseQuery;
import com.spring.cms.service.data.MenuServiceData.NaviMenuData;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.spring.cms.domain.QMenuLink.menuLink;
import static com.spring.cms.domain.menu.QMenu.menu;
import static com.spring.cms.domain.menu.QMenuAuthority.menuAuthority;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MenuQueryDto.CreateResponseQuery findCreatedMenu(Long menuId) {
        return queryFactory
                .select(new QMenuQueryDto_CreateResponseQuery(
                        menu.id,
                        menu.parent.id,
                        menu.top.id,
                        menu.level,
                        menu.ord,
                        menu.name,
                        menu.description,
                        menu.useYn,
                        menu.menuType,
                        menu.boardManager.id,
                        menuLink.link,
                        menuLink.linkTarget.stringValue(),
                        menu.contents.id,
                        menu.createdDate,
                        menu.lastModifiedDate
                ))
                .from(menu)
                .leftJoin(menu.menuLink, menuLink)
                .where(menu.id.eq(menuId))
                .fetchOne();
    }

    @Override
    public List<MenuQueryDto.AllMenusResponseQuery> findMenus(boolean parentIsNull, List<Long> menuIds) {
        return queryFactory
                .select(new QMenuQueryDto_AllMenusResponseQuery(
                        menu.id,
                        menu.menuGroup.id,
                        menu.parent.id,
                        menu.top.id,
                        menu.level,
                        menu.ord,
                        menu.name
                ))
                .from(menu)
                .where(
                        parentIsNull(parentIsNull),
                        parentIdIn(menuIds)
                )
                .orderBy(menu.ord.asc(), menu.createdDate.desc())
                .fetch();
    }

    @Override
    public List<MenuQueryDto.AllMenusResponseQuery> findNaviAllMenus(NaviMenuData naviMenuData) {
        return queryFactory
                .select(new QMenuQueryDto_AllMenusResponseQuery(
                        menu.id,
                        menu.menuGroup.id,
                        menu.parent.id,
                        menu.top.id,
                        menu.level,
                        menu.ord,
                        menu.name
                ))
                .from(menu)
                .where(
                        menu.menuGroup.id.eq(naviMenuData.getMenuGroupId()),
                        menu.useYn.eq('Y'),
                        parentIsNull(naviMenuData.getParentIsNull()),
                        parentIdIn(naviMenuData.getMenuIds())
                )
                .orderBy(menu.ord.asc(), menu.createdDate.desc())
                .fetch();
    }

    @Override
    public List<MenuQueryDto.AllMenusResponseQuery> findNaviMenus(NaviMenuData naviMenuData) {
        return queryFactory
                .select(new QMenuQueryDto_AllMenusResponseQuery(
                        menu.id,
                        menu.menuGroup.id,
                        menu.parent.id,
                        menu.top.id,
                        menu.level,
                        menu.ord,
                        menu.name
                ))
                .from(menuAuthority)
                .join(menuAuthority.menu, menu)
                    .on(menuAuthority.authority.eq(naviMenuData.getAuthority()))
                .where(
                        menu.menuGroup.id.eq(naviMenuData.getMenuGroupId()),
                        menu.useYn.eq('Y'),
                        parentIsNull(naviMenuData.getParentIsNull()),
                        parentIdIn(naviMenuData.getMenuIds())
                )
                .orderBy(menu.ord.asc(), menu.createdDate.desc())
                .fetch();
    }

    private BooleanExpression parentIsNull(boolean parentIsNull) {
        return parentIsNull == true ? menu.parent.isNull() : null;
    }

    private BooleanExpression parentIdIn(List<Long> menuIds) {
        return menuIds != null ? menu.parent.id.in(menuIds) : null;
    }

}
