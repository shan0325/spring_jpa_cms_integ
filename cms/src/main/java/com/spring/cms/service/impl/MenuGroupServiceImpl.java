package com.spring.cms.service.impl;

import com.spring.cms.dto.menu.MenuGroupQueryDto;
import com.spring.cms.repository.menu.MenuGroupRepository;
import com.spring.cms.service.MenuGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MenuGroupServiceImpl implements MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    @Override
    public List<MenuGroupQueryDto.MenuGroupsResponse> getMenuGroups() {
        return menuGroupRepository.findMenuGroups('Y');
    }

}
