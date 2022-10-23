package com.spring.cms.repository.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.cms.dto.menu.MenuGroupQueryDto;
import com.spring.cms.dto.menu.QMenuGroupQueryDto_MenuGroupsResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.spring.cms.domain.QSite.site;
import static com.spring.cms.domain.menu.QMenuGroup.menuGroup;

@RequiredArgsConstructor
public class MenuGroupRepositoryImpl implements MenuGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenuGroupQueryDto.MenuGroupsResponse> findMenuGroups(Character useYn) {
        return queryFactory
                .select(new QMenuGroupQueryDto_MenuGroupsResponse(
                        menuGroup.id,
                        menuGroup.groupName,
                        site.id,
                        site.siteName
                ))
                .from(menuGroup)
                .join(menuGroup.site, site)
                .where(menuGroup.useYn.eq(useYn))
                .fetch();
    }
}
