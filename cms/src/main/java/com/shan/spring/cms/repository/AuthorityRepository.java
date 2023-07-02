package com.shan.spring.cms.repository;

import com.shan.spring.cms.enums.AuthorityTypeEnum;
import com.shan.spring.cms.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByAuthority(String authority);

    Optional<List<Authority>> findByAuthorityType(AuthorityTypeEnum authorityType);

}
