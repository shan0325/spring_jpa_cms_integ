package com.shan.spring.cms.repository.menu;

import com.shan.spring.cms.domain.Authority;
import com.shan.spring.cms.domain.menu.MenuAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuAuthorityRepository extends JpaRepository<MenuAuthority, Long> {

    List<MenuAuthority> findMenuAuthoritiesByAuthority(Authority authority);
}
