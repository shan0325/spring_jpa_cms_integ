package com.spring.cms.config.security;

import com.spring.cms.domain.security.SecuredResource;
import com.spring.cms.domain.security.SecuredResourceAuthority;
import com.spring.cms.repository.security.SecuredResourceAuthorityRepository;
import com.spring.cms.repository.security.SecuredResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SecurityResourceService {

    private final SecuredResourceRepository securedResourceRepository;
    private final SecuredResourceAuthorityRepository securedResourceAuthorityRepository;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<SecuredResource> resources = securedResourceRepository.findAll();

        resources.forEach(resource -> {
            List<ConfigAttribute> configAttributes = new ArrayList<>();

            List<SecuredResourceAuthority> resourceAuthorities = securedResourceAuthorityRepository.findAllBySecuredResource(resource);
            resourceAuthorities.forEach(resourceAuthority -> {
                //ConfigAttribute 타입의 구현체인 SecurityConfig를 넣어준다.
                configAttributes.add(new SecurityConfig(resourceAuthority.getAuthority().getAuthority().name()));
                result.put(new AntPathRequestMatcher(resource.getResourcePattern()), configAttributes);
            });
        });
        return result;
    }
}
