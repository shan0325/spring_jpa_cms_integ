package com.shan.spring.cms.controller;

import com.shan.spring.cms.service.MenuAuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX + "/menu-authorities")
public class MenuAuthorityController extends RestControllerBase {

    private final MenuAuthorityService menuAuthorityService;

    /**
     * 관리자 권한의 메뉴아이디 가져오기
     * @param request
     * @return
     */
    @GetMapping("/is-permit-menu")
    public ResponseEntity<?> isPermitMenu(HttpServletRequest request, @RequestParam Long menuId) {
        return ResponseEntity.ok(menuAuthorityService.isPermitMenu(request, menuId));
    }

}
