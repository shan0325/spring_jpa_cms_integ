package com.spring.cms.service;

import com.spring.cms.dto.VersionInfoDto.VersionInfoResponse;

public interface VersionInfoService {

    VersionInfoResponse getVerionInfo(String name);
}
