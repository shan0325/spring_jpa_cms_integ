package com.shan.spring.cms.service;

import com.shan.spring.cms.dto.VersionInfoDto;

public interface VersionInfoService {

    VersionInfoDto.VersionInfoResponse getVerionInfo(String name);
}
