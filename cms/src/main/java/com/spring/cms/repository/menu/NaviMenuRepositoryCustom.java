package com.spring.cms.repository.menu;

import com.spring.cms.dto.menu.MenuQueryDto;
import com.spring.cms.service.data.MenuServiceData;

import java.util.List;

public interface NaviMenuRepositoryCustom {
    List<MenuQueryDto.NaviMenusResponseQuery> findAllNaviMenus(MenuServiceData.NaviMenuData naviMenuData);
    List<MenuQueryDto.NaviMenusResponseQuery> findNaviMenus(MenuServiceData.NaviMenuData naviMenuData);
}
