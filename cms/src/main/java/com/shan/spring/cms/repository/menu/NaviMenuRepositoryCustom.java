package com.shan.spring.cms.repository.menu;

import com.shan.spring.cms.repository.menu.dto.MenuQueryDto;
import com.shan.spring.cms.service.data.MenuServiceData;

import java.util.List;

public interface NaviMenuRepositoryCustom {
    List<MenuQueryDto.NaviMenusResponseQuery> findAllNaviMenus(MenuServiceData.NaviMenuData naviMenuData);
    List<MenuQueryDto.NaviMenusResponseQuery> findNaviMenus(MenuServiceData.NaviMenuData naviMenuData);
}
