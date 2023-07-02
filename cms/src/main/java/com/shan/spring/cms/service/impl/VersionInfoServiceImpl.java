package com.shan.spring.cms.service.impl;

import com.shan.spring.cms.dto.VersionInfoDto;
import com.shan.spring.cms.repository.VersionInfoRepository;
import com.shan.spring.cms.domain.VersionInfo;
import com.shan.spring.cms.service.VersionInfoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VersionInfoServiceImpl implements VersionInfoService {

    private final VersionInfoRepository versionInfoRepository;

    private final ModelMapper modelMapper;

    @Override
    public VersionInfoDto.VersionInfoResponse getVerionInfo(String name) {
        VersionInfo versionInfo = versionInfoRepository.findByName(name)
                .orElse(VersionInfo.createEmptyVersionInfo());

        return modelMapper.map(versionInfo, VersionInfoDto.VersionInfoResponse.class);
    }
}
