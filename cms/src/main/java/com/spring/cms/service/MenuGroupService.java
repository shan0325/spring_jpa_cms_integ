package com.spring.cms.service;

import com.spring.cms.dto.menu.MenuGroupQueryDto;

import java.util.List;

public interface MenuGroupService {

    List<MenuGroupQueryDto.MenuGroupsResponse> getMenuGroups();
}
