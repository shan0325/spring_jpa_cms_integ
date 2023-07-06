package com.shan.spring.cms.repository.menu;

import com.shan.spring.cms.repository.menu.dto.MenuQueryDto;

import java.util.List;

public interface MenuRepositoryCustom {
    MenuQueryDto.CreateResponseQuery findCreatedMenu(Long id);
    List<MenuQueryDto.AllMenusResponseQuery> findMenus(boolean parentIsNull, List<Long> menuIds);
}