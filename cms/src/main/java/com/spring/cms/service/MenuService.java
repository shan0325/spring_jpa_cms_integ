package com.spring.cms.service;

import com.spring.cms.dto.menu.MenuDto;
import com.spring.cms.dto.menu.MenuQueryDto;

import java.util.List;

public interface MenuService {
    void createMenus(MenuDto.Create create);
    void updateMenu(Long menuId, MenuDto.Update update);
    void deleteMenu(Long menuId);
    MenuDto.MenuDetailResponse getMenuDetail(Long menuId);
    List<MenuDto.AllMenusResponse> getAllMenus();
    List<MenuQueryDto.AllMenusResponseQuery> getAllMenusOpti();
    List<MenuDto.AllMenusResponse> getMenusByMenuGroupId(Long menuGroupId);
    List<MenuQueryDto.AllMenusResponseQuery> getMenusByMenuGroupIdAndManagerId(Long menuGroupId, Long managerId);
}
