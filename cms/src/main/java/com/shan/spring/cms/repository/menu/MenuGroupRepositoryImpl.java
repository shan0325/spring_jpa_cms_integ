package com.shan.spring.cms.repository.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shan.spring.cms.domain.QSite;
import com.shan.spring.cms.domain.menu.QMenuGroup;
import com.shan.spring.cms.repository.menu.dto.MenuGroupQueryDto;
import com.shan.spring.cms.repository.menu.dto.QMenuGroupQueryDto_MenuGroupsResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MenuGroupRepositoryImpl implements MenuGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenuGroupQueryDto.MenuGroupsResponse> findMenuGroups(Character useYn) {
        return queryFactory
                .select(new QMenuGroupQueryDto_MenuGroupsResponse(
                        QMenuGroup.menuGroup.id,
                        QMenuGroup.menuGroup.groupName,
                        QSite.site.id,
                        QSite.site.siteName
                ))
                .from(QMenuGroup.menuGroup)
                .join(QMenuGroup.menuGroup.site, QSite.site)
                .where(QMenuGroup.menuGroup.useYn.eq(useYn))
                .fetch();
    }
}
