package com.spring.cms.service;

import com.spring.cms.repository.menu.dto.MenuQueryDto;

import java.util.List;

public interface NaviMenuService {
    List<MenuQueryDto.NaviMenusResponseQuery> getNaviMenus(Long menuGroupId, Long managerId);
}
