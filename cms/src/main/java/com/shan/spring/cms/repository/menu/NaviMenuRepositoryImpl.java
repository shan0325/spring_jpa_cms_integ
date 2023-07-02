package com.shan.spring.cms.repository.menu;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shan.spring.cms.domain.menu.QMenu;
import com.shan.spring.cms.domain.menu.QMenuAuthority;
import com.shan.spring.cms.repository.menu.dto.MenuQueryDto;
import com.shan.spring.cms.repository.menu.dto.QMenuQueryDto_NaviMenusResponseQuery;
import com.shan.spring.cms.service.data.MenuServiceData;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class NaviMenuRepositoryImpl implements NaviMenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenuQueryDto.NaviMenusResponseQuery> findAllNaviMenus(MenuServiceData.NaviMenuData naviMenuData) {
        return queryFactory
                .select(new QMenuQueryDto_NaviMenusResponseQuery(
                        QMenu.menu.id,
                        QMenu.menu.menuGroup.id,
                        QMenu.menu.parent.id,
                        QMenu.menu.top.id,
                        QMenu.menu.level,
                        QMenu.menu.ord,
                        QMenu.menu.name,
                        QMenu.menu.menuType,
                        QMenu.menu.materialIcon,
                        QMenu.menu.viewPath
                ))
                .from(QMenu.menu)
                .where(
                        QMenu.menu.menuGroup.id.eq(naviMenuData.getMenuGroupId()),
                        QMenu.menu.useYn.eq(naviMenuData.getUseYn()),
                        parentIsNull(naviMenuData.getParentIsNull()),
                        parentIdIn(naviMenuData.getMenuIds())
                )
                .orderBy(QMenu.menu.ord.asc(), QMenu.menu.createdDate.desc())
                .fetch();
    }

    @Override
    public List<MenuQueryDto.NaviMenusResponseQuery> findNaviMenus(MenuServiceData.NaviMenuData naviMenuData) {
        return queryFactory
                .select(new QMenuQueryDto_NaviMenusResponseQuery(
                        QMenu.menu.id,
                        QMenu.menu.menuGroup.id,
                        QMenu.menu.parent.id,
                        QMenu.menu.top.id,
                        QMenu.menu.level,
                        QMenu.menu.ord,
                        QMenu.menu.name,
                        QMenu.menu.menuType,
                        QMenu.menu.materialIcon,
                        QMenu.menu.viewPath
                ))
                .from(QMenuAuthority.menuAuthority)
                .join(QMenuAuthority.menuAuthority.menu, QMenu.menu)
                .on(QMenuAuthority.menuAuthority.authority.eq(naviMenuData.getAuthority()))
                .where(
                        QMenu.menu.menuGroup.id.eq(naviMenuData.getMenuGroupId()),
                        QMenu.menu.useYn.eq(naviMenuData.getUseYn()),
                        parentIsNull(naviMenuData.getParentIsNull()),
                        parentIdIn(naviMenuData.getMenuIds())
                )
                .orderBy(QMenu.menu.ord.asc(), QMenu.menu.createdDate.desc())
                .fetch();
    }

    private BooleanExpression parentIsNull(boolean parentIsNull) {
        return parentIsNull == true ? QMenu.menu.parent.isNull() : null;
    }

    private BooleanExpression parentIdIn(List<Long> menuIds) {
        return menuIds != null ? QMenu.menu.parent.id.in(menuIds) : null;
    }
}
