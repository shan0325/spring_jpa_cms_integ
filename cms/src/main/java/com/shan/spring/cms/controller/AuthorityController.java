package com.shan.spring.cms.controller;

import com.shan.spring.cms.enums.AuthorityTypeEnum;
import com.shan.spring.cms.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX)
public class AuthorityController {

    private final AuthorityService authorityService;

    @GetMapping("/authorities")
    public ResponseEntity<?> getAuthorities(@RequestParam AuthorityTypeEnum authorityType) {
        return ResponseEntity.ok(authorityService.getAuthorities(authorityType));
    }
}
