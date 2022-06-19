package com.spring.cms.repository;

import com.spring.cms.domain.Authority;
import com.spring.cms.enums.AuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByAuthority(String authority);

    Optional<List<Authority>> findByAuthorityType(AuthorityType authorityType);

}
