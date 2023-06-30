package com.spring.cms.service;

import com.spring.cms.repository.menu.dto.MenuGroupQueryDto;

import java.util.List;

public interface MenuGroupService {

    List<MenuGroupQueryDto.MenuGroupsResponse> getMenuGroups();
}
