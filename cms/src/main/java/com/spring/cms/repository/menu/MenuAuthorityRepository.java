package com.spring.cms.repository.menu;

import com.spring.cms.domain.Authority;
import com.spring.cms.domain.menu.MenuAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuAuthorityRepository extends JpaRepository<MenuAuthority, Long> {

    List<MenuAuthority> findMenuAuthoritiesByAuthority(Authority authority);
}
