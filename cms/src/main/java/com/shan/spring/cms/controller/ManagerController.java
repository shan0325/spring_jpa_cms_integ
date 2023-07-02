package com.shan.spring.cms.controller;

import com.shan.spring.cms.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX)
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/manager/auth/{username}")
    public ResponseEntity<?> getAuthMember(@PathVariable String username) {
        return ResponseEntity.ok(managerService.getAuthManager(username));
    }
}
