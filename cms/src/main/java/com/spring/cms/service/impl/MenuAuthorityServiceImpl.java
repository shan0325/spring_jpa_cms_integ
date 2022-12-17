package com.spring.cms.service.impl;

import com.spring.cms.config.security.JwtProvider;
import com.spring.cms.domain.Authority;
import com.spring.cms.domain.Manager;
import com.spring.cms.domain.menu.MenuAuthority;
import com.spring.cms.enums.AuthorityEnum;
import com.spring.cms.exception.ManagerException;
import com.spring.cms.repository.ManagerRepository;
import com.spring.cms.repository.menu.MenuAuthorityRepository;
import com.spring.cms.service.MenuAuthorityService;
import com.spring.cms.service.data.MenuServiceData;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.spring.cms.exception.ManagerException.ManagerExceptionType.NOT_FOUND_MANAGER;

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
                .orElseThrow(() -> new ManagerException(NOT_FOUND_MANAGER));

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
