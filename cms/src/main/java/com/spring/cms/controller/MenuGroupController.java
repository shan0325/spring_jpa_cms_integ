package com.spring.cms.controller;

import com.spring.cms.service.MenuGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX)
public class MenuGroupController {

    private final MenuGroupService menuGroupService;

    @GetMapping("/menu-groups")
    public ResponseEntity<?> getMenuGroups() {
        return ResponseEntity.ok(menuGroupService.getMenuGroups());
    }
}
