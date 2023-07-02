package com.shan.spring.cms.repository;

import com.shan.spring.cms.domain.VersionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VersionInfoRepository extends JpaRepository<VersionInfo, Long> {

    Optional<VersionInfo> findByName(String name);
}
