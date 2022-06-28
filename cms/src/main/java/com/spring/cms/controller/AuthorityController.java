package com.spring.cms.controller;

import com.spring.cms.domain.Authority;
import com.spring.cms.enums.AuthorityType;
import com.spring.cms.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX)
public class AuthorityController {

    private final AuthorityService authorityService;

    @GetMapping("/authorities")
    public ResponseEntity<?> getAuthorities(@RequestParam AuthorityType authorityType) {
        return ResponseEntity.ok(authorityService.getAuthorities(authorityType));
    }
}
