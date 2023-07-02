package com.shan.spring.cms.repository.menu;

import com.shan.spring.cms.domain.menu.Menu;
import com.shan.spring.cms.domain.menu.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {
    Optional<List<Menu>> findByParentOrderByOrd(Menu parent);
    @Query("select m " +
            "from Menu m " +
            "where (m.parent is null or m.parent = :parent) " +
            "and m.menuGroup = :menuGroup " +
            "order by m.ord")
    Optional<List<Menu>> findMenusByMenuGroup(@Param("parent") Menu parent, @Param("menuGroup") MenuGroup menuGroup);
    Integer countByParent(Menu parent);
}
