package com.shan.spring.cms.repository.menu;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shan.spring.cms.domain.menu.QMenu;
import com.shan.spring.cms.domain.menu.QMenuLink;
import com.shan.spring.cms.repository.menu.dto.MenuQueryDto;
import com.shan.spring.cms.repository.menu.dto.QMenuQueryDto_AllMenusResponseQuery;
import com.shan.spring.cms.repository.menu.dto.QMenuQueryDto_CreateResponseQuery;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MenuQueryDto.CreateResponseQuery findCreatedMenu(Long menuId) {
        return queryFactory
                .select(new QMenuQueryDto_CreateResponseQuery(
                        QMenu.menu.id,
                        QMenu.menu.parent.id,
                        QMenu.menu.top.id,
                        QMenu.menu.level,
                        QMenu.menu.ord,
                        QMenu.menu.name,
                        QMenu.menu.description,
                        QMenu.menu.useYn,
                        QMenu.menu.menuType,
                        QMenu.menu.boardManager.id,
                        QMenuLink.menuLink.link,
                        QMenuLink.menuLink.linkTarget.stringValue(),
                        QMenu.menu.contents.id,
                        QMenu.menu.createdDate,
                        QMenu.menu.lastModifiedDate
                ))
                .from(QMenu.menu)
                .leftJoin(QMenu.menu.menuLink, QMenuLink.menuLink)
                .where(QMenu.menu.id.eq(menuId))
                .fetchOne();
    }

    @Override
    public List<MenuQueryDto.AllMenusResponseQuery> findMenus(boolean parentIsNull, List<Long> menuIds) {
        return queryFactory
                .select(new QMenuQueryDto_AllMenusResponseQuery(
                        QMenu.menu.id,
                        QMenu.menu.menuGroup.id,
                        QMenu.menu.parent.id,
                        QMenu.menu.top.id,
                        QMenu.menu.level,
                        QMenu.menu.ord,
                        QMenu.menu.name
                ))
                .from(QMenu.menu)
                .where(
                        parentIsNull(parentIsNull),
                        parentIdIn(menuIds)
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
