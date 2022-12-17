package com.spring.cms.controller;

import com.spring.cms.dto.menu.MenuDto;
import com.spring.cms.service.MenuService;
import com.spring.cms.service.NaviMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX)
public class MenuController {

    private final MenuService menuService;
    private final NaviMenuService naviMenuService;

    @PostMapping("/menus")
    public ResponseEntity<?> createMenu(@RequestBody @Valid MenuDto.Create create) {
        log.info("Request Param [{}]", create);

        menuService.createMenus(create);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/menus/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable Long menuId, @RequestBody @Valid MenuDto.Update update) {
        log.info("Request Param [{}]", update);

        menuService.updateMenu(menuId, update);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/menus/{menuId}")
    public ResponseEntity<?> getMenuDetail(@PathVariable Long menuId) {
        return ResponseEntity.ok(menuService.getMenuDetail(menuId));
    }

    @GetMapping("/menus")
    public ResponseEntity<?> getMenus() {
        return ResponseEntity.ok(menuService.getAllMenus());
    }

    @GetMapping("/menus/opti")
    public ResponseEntity<?> getMenusOpti() {
        return ResponseEntity.ok(menuService.getAllMenusOpti());
    }

    @GetMapping("/menus/menu-groups/{menuGroupId}")
    public ResponseEntity<?> getMenusByMenuGroupId(@PathVariable Long menuGroupId) {
        return ResponseEntity.ok(menuService.getMenusByMenuGroupId(menuGroupId));
    }

    @GetMapping("/menus/navi-menus")
    public ResponseEntity<?> getNaviMenus(@RequestParam Long menuGroupId, @RequestParam Long managerId) {
        return ResponseEntity.ok(naviMenuService.getNaviMenus(menuGroupId, managerId));
    }

}
