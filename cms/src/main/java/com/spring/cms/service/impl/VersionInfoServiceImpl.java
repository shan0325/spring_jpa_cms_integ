package com.spring.cms.service.impl;

import com.spring.cms.domain.VersionInfo;
import com.spring.cms.dto.VersionInfoDto.VersionInfoResponse;
import com.spring.cms.repository.VersionInfoRepository;
import com.spring.cms.service.VersionInfoService;
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
    public VersionInfoResponse getVerionInfo(String name) {
        VersionInfo versionInfo = versionInfoRepository.findByName(name)
                .orElse(VersionInfo.createEmptyVersionInfo());

        return modelMapper.map(versionInfo, VersionInfoResponse.class);
    }
}
