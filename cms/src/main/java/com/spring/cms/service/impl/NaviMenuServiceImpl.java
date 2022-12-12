package com.spring.cms.service.impl;

import com.spring.cms.domain.Authority;
import com.spring.cms.dto.menu.MenuQueryDto;
import com.spring.cms.enums.AuthorityEnum;
import com.spring.cms.repository.menu.MenuRepository;
import com.spring.cms.service.ManagerService;
import com.spring.cms.service.NaviMenuService;
import com.spring.cms.service.data.MenuServiceData;
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

    private final MenuRepository menuRepository;
    private final ManagerService managerService;

    @Override
    public List<MenuQueryDto.AllMenusResponseQuery> getNaviMenus(Long menuGroupId, Long managerId) {
        Authority authority = getAuthorityByManagerId(managerId);
        if (authority == null) return null;

        List<MenuQueryDto.AllMenusResponseQuery> menus = getNaviTopMenus(menuGroupId, authority);
        setNaviChildMenusByRecursive(menuGroupId, authority, menus);
        return menus;
    }

    private Authority getAuthorityByManagerId(Long managerId) {
        return managerService.getManagerByManagerId(managerId)
                .getAuthority();
    }

    private List<MenuQueryDto.AllMenusResponseQuery> getNaviTopMenus(Long menuGroupId, Authority authority) {
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
                .build();
    }

    private void setNaviChildMenusByRecursive(Long menuGroupId, Authority authority, List<MenuQueryDto.AllMenusResponseQuery> menus) {
        if (menus == null || menus.isEmpty()) return;

        List<MenuQueryDto.AllMenusResponseQuery> childMenus = getNaviChildMenus(menuGroupId, authority, menus);
        setNaviMenusSetChildMenus(menus, childMenus);
        setNaviChildMenusByRecursive(menuGroupId, authority, childMenus);
    }

    private List<MenuQueryDto.AllMenusResponseQuery> getNaviChildMenus(Long menuGroupId, Authority authority, List<MenuQueryDto.AllMenusResponseQuery> menus) {
        MenuServiceData.NaviMenuData naviMenuData = createNaviMenuData(menuGroupId, authority, false, toMenusIds(menus));
        return getNaviMenusByNaviMenuData(naviMenuData);
    }

    private void setNaviMenusSetChildMenus(List<MenuQueryDto.AllMenusResponseQuery> menus, List<MenuQueryDto.AllMenusResponseQuery> childMenus) {
        Map<Long, List<MenuQueryDto.AllMenusResponseQuery>> childMenusMap = childMenus.stream()
                .collect(Collectors.groupingBy(allMenusResponseQuery -> allMenusResponseQuery.getParentId()));

        menus.forEach(m -> m.setChildMenus(childMenusMap.get(m.getId())));
    }

    private List<MenuQueryDto.AllMenusResponseQuery> getNaviMenusByNaviMenuData(MenuServiceData.NaviMenuData naviMenuData) {
        List<MenuQueryDto.AllMenusResponseQuery> menus;
        if (isAdminAuthority(naviMenuData)) {
            menus = menuRepository.findNaviAllMenus(naviMenuData);
        } else {
            menus = menuRepository.findNaviMenus(naviMenuData);
        }
        return menus;
    }

    private boolean isAdminAuthority(MenuServiceData.NaviMenuData naviMenuData) {
        return AuthorityEnum.ROLE_ADMIN == naviMenuData.getAuthorityEnum();
    }

    private List<Long> toMenusIds(List<MenuQueryDto.AllMenusResponseQuery> menus) {
        return menus.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());
    }
}
