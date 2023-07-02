package com.shan.spring.cms.repository;

import com.shan.spring.cms.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByUsername(String username);

    boolean existsByUsername(String username);

}
