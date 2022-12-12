package com.spring.cms.repository.menu;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.cms.dto.menu.MenuQueryDto;
import com.spring.cms.dto.menu.QMenuQueryDto_AllMenusResponseQuery;
import com.spring.cms.dto.menu.QMenuQueryDto_NaviMenusResponseQuery;
import com.spring.cms.service.data.MenuServiceData;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.spring.cms.domain.menu.QMenu.menu;
import static com.spring.cms.domain.menu.QMenuAuthority.menuAuthority;

@RequiredArgsConstructor
public class NaviMenuRepositoryImpl implements NaviMenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenuQueryDto.NaviMenusResponseQuery> findAllNaviMenus(MenuServiceData.NaviMenuData naviMenuData) {
        return queryFactory
                .select(new QMenuQueryDto_NaviMenusResponseQuery(
                        menu.id,
                        menu.menuGroup.id,
                        menu.parent.id,
                        menu.top.id,
                        menu.level,
                        menu.ord,
                        menu.name,
                        menu.materialIcon
                ))
                .from(menu)
                .where(
                        menu.menuGroup.id.eq(naviMenuData.getMenuGroupId()),
                        menu.useYn.eq(naviMenuData.getUseYn()),
                        parentIsNull(naviMenuData.getParentIsNull()),
                        parentIdIn(naviMenuData.getMenuIds())
                )
                .orderBy(menu.ord.asc(), menu.createdDate.desc())
                .fetch();
    }

    @Override
    public List<MenuQueryDto.NaviMenusResponseQuery> findNaviMenus(MenuServiceData.NaviMenuData naviMenuData) {
        return queryFactory
                .select(new QMenuQueryDto_NaviMenusResponseQuery(
                        menu.id,
                        menu.menuGroup.id,
                        menu.parent.id,
                        menu.top.id,
                        menu.level,
                        menu.ord,
                        menu.name,
                        menu.materialIcon
                ))
                .from(menuAuthority)
                .join(menuAuthority.menu, menu)
                .on(menuAuthority.authority.eq(naviMenuData.getAuthority()))
                .where(
                        menu.menuGroup.id.eq(naviMenuData.getMenuGroupId()),
                        menu.useYn.eq(naviMenuData.getUseYn()),
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
