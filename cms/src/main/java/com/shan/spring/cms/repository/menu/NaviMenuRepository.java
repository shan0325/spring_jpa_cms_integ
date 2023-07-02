package com.shan.spring.cms.repository.menu;

import com.shan.spring.cms.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaviMenuRepository extends JpaRepository<Menu, Long>, NaviMenuRepositoryCustom {
}
