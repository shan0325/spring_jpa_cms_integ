package com.spring.cms.repository;

import com.spring.cms.domain.Authority;
import com.spring.cms.enums.AuthorityTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByAuthority(String authority);

    Optional<List<Authority>> findByAuthorityType(AuthorityTypeEnum authorityType);

}
