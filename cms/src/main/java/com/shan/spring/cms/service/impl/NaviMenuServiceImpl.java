package com.shan.spring.cms.service.impl;

import com.shan.spring.cms.enums.AuthorityEnum;
import com.shan.spring.cms.repository.menu.NaviMenuRepository;
import com.shan.spring.cms.repository.menu.dto.MenuQueryDto;
import com.shan.spring.cms.domain.Authority;
import com.shan.spring.cms.service.ManagerService;
import com.shan.spring.cms.service.NaviMenuService;
import com.shan.spring.cms.service.data.MenuServiceData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NaviMenuServiceImpl implements NaviMenuService {

    private final NaviMenuRepository naviMenuRepository;
    private final ManagerService managerService;

    @Override
    public List<MenuQueryDto.NaviMenusResponseQuery> getNaviMenus(Long menuGroupId, Long managerId) {
        Authority authority = getAuthorityByManagerId(managerId);
        if (authority == null) return null;

        List<MenuQueryDto.NaviMenusResponseQuery> menus = getNaviTopMenus(menuGroupId, authority);
        setNaviChildMenusByRecursive(menuGroupId, authority, menus);
        return menus;
    }

    private Authority getAuthorityByManagerId(Long managerId) {
        return managerService.getManagerByManagerId(managerId)
                .getAuthority();
    }

    private List<MenuQueryDto.NaviMenusResponseQuery> getNaviTopMenus(Long menuGroupId, Authority authority) {
        MenuServiceData.NaviMenuData naviMenuData = createNaviMenuData(menuGroupId, authority, true, null);
        return getNaviMenusByNaviMenuData(naviMenuData);
    }

    private MenuServiceData.NaviMenuData createNaviMenuData(Long menuGroupId, Authority authority, Boolean parentIsNull, List<Long> menuIds) {
        return MenuServiceData.NaviMenuData.builder()
                .menuGroupId(menuGroupId)
                .authority(authority)
                .authorityEnum(AuthorityEnum.valueOf(authority.getAuthority()))
                .parentIsNull(parentIsNull)
                .menuIds(menuIds)
                .useYn('Y')
                .build();
    }

    private void setNaviChildMenusByRecursive(Long menuGroupId, Authority authority, List<MenuQueryDto.NaviMenusResponseQuery> menus) {
        if (menus == null || menus.isEmpty()) return;

        List<MenuQueryDto.NaviMenusResponseQuery> childMenus = getNaviChildMenus(menuGroupId, authority, menus);
        setNaviMenusSetChildMenus(menus, childMenus);
        setNaviChildMenusByRecursive(menuGroupId, authority, childMenus);
    }

    private List<MenuQueryDto.NaviMenusResponseQuery> getNaviChildMenus(Long menuGroupId, Authority authority, List<MenuQueryDto.NaviMenusResponseQuery> menus) {
        MenuServiceData.NaviMenuData naviMenuData = createNaviMenuData(menuGroupId, authority, false, toMenusIds(menus));
        return getNaviMenusByNaviMenuData(naviMenuData);
    }

    private void setNaviMenusSetChildMenus(List<MenuQueryDto.NaviMenusResponseQuery> menus, List<MenuQueryDto.NaviMenusResponseQuery> childMenus) {
        Map<Long, List<MenuQueryDto.NaviMenusResponseQuery>> childMenusMap = childMenus.stream()
                .collect(Collectors.groupingBy(naviMenusResponseQuery -> naviMenusResponseQuery.getParentId()));

        menus.forEach(m -> m.setChildMenus(childMenusMap.get(m.getId())));
    }

    private List<MenuQueryDto.NaviMenusResponseQuery> getNaviMenusByNaviMenuData(MenuServiceData.NaviMenuData naviMenuData) {
        if (isAdminAuthority(naviMenuData)) {
            return naviMenuRepository.findAllNaviMenus(naviMenuData);
        } else {
            return  naviMenuRepository.findNaviMenus(naviMenuData);
        }
    }

    private boolean isAdminAuthority(MenuServiceData.NaviMenuData naviMenuData) {
        return AuthorityEnum.ROLE_ADMIN == naviMenuData.getAuthorityEnum();
    }

    private List<Long> toMenusIds(List<MenuQueryDto.NaviMenusResponseQuery> menus) {
        return menus.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());
    }
}
