package com.spring.cms.repository.menu;

import com.spring.cms.domain.Authority;
import com.spring.cms.dto.menu.MenuQueryDto;

import java.util.List;

public interface MenuRepositoryCustom {
    MenuQueryDto.CreateResponseQuery findCreatedMenu(Long id);
    List<MenuQueryDto.AllMenusResponseQuery> findMenus(boolean parentIsNull, List<Long> menuIds);

    List<MenuQueryDto.AllMenusResponseQuery> findLeftTopMenusByAuthority(Authority authority, boolean parentIsNull, List<Long> menuIds);
}
