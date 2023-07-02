package com.shan.spring.cms.service;

import com.shan.spring.cms.repository.menu.dto.MenuGroupQueryDto;

import java.util.List;

public interface MenuGroupService {

    List<MenuGroupQueryDto.MenuGroupsResponse> getMenuGroups();
}
