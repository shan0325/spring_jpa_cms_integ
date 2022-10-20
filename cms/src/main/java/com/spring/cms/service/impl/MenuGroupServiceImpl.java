package com.spring.cms.service.impl;

import com.spring.cms.repository.menu.MenuGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MenuGroupServiceImpl {

    private final MenuGroupRepository menuGroupRepository;


}
