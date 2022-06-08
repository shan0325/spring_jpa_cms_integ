package com.spring.cms.config.security;

import com.spring.cms.domain.Manager;
import com.spring.cms.domain.Member;
import com.spring.cms.exception.ManagerException;
import com.spring.cms.exception.MemberException;
import com.spring.cms.repository.ManagerRepository;
import com.spring.cms.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.spring.cms.exception.ManagerException.ManagerExceptionType.NOT_FOUND_MANAGER;
import static com.spring.cms.exception.MemberException.MemberExceptionType.NOT_FOUND_MEMBER;


@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return managerRepository.findByUsername(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new ManagerException(NOT_FOUND_MANAGER));
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Manager manager) {
        List<SimpleGrantedAuthority> roles = manager.getManagerAuthorities().stream()
                .map(ma -> new SimpleGrantedAuthority(ma.getAuthority().getAuthority()))
                .collect(Collectors.toList());

        return new User(manager.getUsername(), manager.getPassword(), roles);
    }
}
