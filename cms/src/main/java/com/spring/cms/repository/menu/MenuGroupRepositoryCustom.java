package com.spring.cms.repository.menu;

import com.spring.cms.dto.menu.MenuGroupQueryDto;

import java.util.List;

public interface MenuGroupRepositoryCustom {

    List<MenuGroupQueryDto.MenuGroupsResponse> findMenuGroups(Character useYn);
}
