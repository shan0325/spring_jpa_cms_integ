package com.shan.spring.cms.repository.security;

import com.shan.spring.cms.domain.security.SecuredResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecuredResourceRepository extends JpaRepository<SecuredResource, Long> {

}
