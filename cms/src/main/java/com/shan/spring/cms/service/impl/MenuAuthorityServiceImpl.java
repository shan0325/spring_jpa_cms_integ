package com.shan.spring.cms.service.impl;

import com.shan.spring.config.security.JwtProvider;
import com.shan.spring.cms.enums.AuthorityEnum;
import com.shan.spring.cms.exception.ManagerException;
import com.shan.spring.cms.repository.ManagerRepository;
import com.shan.spring.cms.repository.menu.MenuAuthorityRepository;
import com.shan.spring.cms.domain.Authority;
import com.shan.spring.cms.domain.Manager;
import com.shan.spring.cms.service.MenuAuthorityService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MenuAuthorityServiceImpl implements MenuAuthorityService {

    private final MenuAuthorityRepository menuAuthorityRepository;
    private final ManagerRepository managerRepository;
    private final JwtProvider jwtProvider;

    @Override
    public boolean isPermitMenu(HttpServletRequest request, Long menuId) {
        Claims claims = jwtProvider.parseClaims(jwtProvider.resolveToken(request));
        String username = claims.getSubject();

        Manager manager = managerRepository.findByUsername(username)
                .orElseThrow(() -> new ManagerException(ManagerException.ManagerExceptionType.NOT_FOUND_MANAGER));

        Authority authority = manager.getAuthority();
        if (isAdmin(authority)) return true;

        return isPermitMenuByAuthority(menuId, authority);
    }

    private boolean isPermitMenuByAuthority(Long menuId, Authority authority) {
        long permitCount = menuAuthorityRepository.findMenuAuthoritiesByAuthority(authority).stream()
                .filter(ma -> ma.getMenu().getId() == menuId)
                .count();

        return permitCount == 0 ? false : true;
    }

    private boolean isAdmin(Authority authority) {
        if (AuthorityEnum.ROLE_ADMIN.name().equals(authority.getAuthority())) {
            return true;
        }
        return false;
    }
}
