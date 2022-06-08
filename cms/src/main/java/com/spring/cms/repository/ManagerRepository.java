package com.spring.cms.repository;

import com.spring.cms.domain.Manager;
import com.spring.cms.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByUsername(String username);

    boolean existsByUsername(String username);

}
