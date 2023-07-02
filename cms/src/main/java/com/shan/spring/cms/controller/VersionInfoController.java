package com.shan.spring.cms.controller;

import com.shan.spring.cms.dto.VersionInfoDto;
import com.shan.spring.cms.service.VersionInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX + "/version-info")
public class VersionInfoController {

    private final VersionInfoService versionInfoService;

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getVersionInfo(@PathVariable String name) {
        VersionInfoDto.VersionInfoResponse verionInfo = versionInfoService.getVerionInfo(name);
        System.out.println("verionInfo = " + verionInfo);
        return ResponseEntity.ok(verionInfo);
    }

}
