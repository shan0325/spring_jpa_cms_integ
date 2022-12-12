package com.spring.cms.service;

import com.spring.cms.dto.menu.MenuQueryDto;

import java.util.List;

public interface NaviMenuService {
    List<MenuQueryDto.AllMenusResponseQuery> getNaviMenus(Long menuGroupId, Long managerId);
}
