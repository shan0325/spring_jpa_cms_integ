package com.spring.cms.repository.menu;

import com.spring.cms.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaviMenuRepository extends JpaRepository<Menu, Long>, NaviMenuRepositoryCustom {
}
