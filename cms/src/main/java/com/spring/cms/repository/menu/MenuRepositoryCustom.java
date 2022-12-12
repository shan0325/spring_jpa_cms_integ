package com.spring.cms.repository.menu;

import com.spring.cms.dto.menu.MenuQueryDto;
import com.spring.cms.service.data.MenuServiceData.NaviMenuData;

import java.util.List;

public interface MenuRepositoryCustom {
    MenuQueryDto.CreateResponseQuery findCreatedMenu(Long id);
    List<MenuQueryDto.AllMenusResponseQuery> findMenus(boolean parentIsNull, List<Long> menuIds);
    List<MenuQueryDto.AllMenusResponseQuery> findNaviAllMenus(NaviMenuData naviMenuData);
    List<MenuQueryDto.AllMenusResponseQuery> findNaviMenus(NaviMenuData naviMenuData);
}
