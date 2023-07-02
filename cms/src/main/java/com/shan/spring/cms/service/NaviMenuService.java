package com.shan.spring.cms.service;

import com.shan.spring.cms.repository.menu.dto.MenuQueryDto;

import java.util.List;

public interface NaviMenuService {
    List<MenuQueryDto.NaviMenusResponseQuery> getNaviMenus(Long menuGroupId, Long managerId);
}
