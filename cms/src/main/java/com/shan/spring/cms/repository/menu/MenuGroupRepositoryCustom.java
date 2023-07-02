package com.shan.spring.cms.repository.menu;

import com.shan.spring.cms.repository.menu.dto.MenuGroupQueryDto;

import java.util.List;

public interface MenuGroupRepositoryCustom {

    List<MenuGroupQueryDto.MenuGroupsResponse> findMenuGroups(Character useYn);
}
