package com.shan.spring.cms.config.security;

import com.shan.spring.cms.domain.Authority;
import com.shan.spring.cms.domain.security.SecuredResource;
import com.shan.spring.cms.domain.security.SecuredResourceAuthority;
import com.shan.spring.cms.repository.security.SecuredResourceAuthorityRepository;
import com.shan.spring.cms.repository.security.SecuredResourceRepository;
import com.shan.spring.config.security.SecurityResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.transaction.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest
@Transactional
class SecurityResourceServiceTest {

    @Autowired
    private SecurityResourceService securityResourceService;

    @Autowired
    private SecuredResourceRepository securedResourceRepository;

    @Autowired
    private SecuredResourceAuthorityRepository securedResourceAuthorityRepository;

    @Test
    public void findResources() {
        List<SecuredResource> resources = securedResourceRepository.findAll();

        resources.forEach(resource -> {
            List<SecuredResourceAuthority> resourceAuthorities = securedResourceAuthorityRepository.findAllBySecuredResource(resource);
            resourceAuthorities.forEach(resourceAuthority -> {
                Authority authority = resourceAuthority.getAuthority();
                String authorityName = authority.getAuthorityName();
                System.out.println("authorityName = " + authorityName);
            });
        });
    }

    @Test
    public void getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceList = securityResourceService.getResourceList();
        System.out.println("resourceList = " + resourceList);
    }

}