package com.spring.cms.repository.menu;

import com.spring.cms.domain.menu.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long>, MenuGroupRepositoryCustom {

}
