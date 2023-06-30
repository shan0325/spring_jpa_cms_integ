package com.spring.cms.service;

import com.spring.cms.dto.MenuDto;
import com.spring.cms.repository.menu.dto.MenuQueryDto;

import java.util.List;

public interface MenuService {
    void createMenus(MenuDto.Create create);
    void updateMenu(Long menuId, MenuDto.Update update);
    void deleteMenu(Long menuId);
    MenuDto.MenuDetailResponse getMenuDetail(Long menuId);
    List<MenuDto.AllMenusResponse> getAllMenus();
    List<MenuQueryDto.AllMenusResponseQuery> getAllMenusOpti();
    List<MenuDto.AllMenusResponse> getMenusByMenuGroupId(Long menuGroupId);
}
