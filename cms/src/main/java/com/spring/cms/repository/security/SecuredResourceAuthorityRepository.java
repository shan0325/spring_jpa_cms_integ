package com.spring.cms.repository.security;

import com.spring.cms.domain.security.SecuredResourceAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecuredResourceAuthorityRepository extends JpaRepository<SecuredResourceAuthority, Long> {

    List<SecuredResourceAuthority> findAllBySecuredResource(Long resourceId);
}
