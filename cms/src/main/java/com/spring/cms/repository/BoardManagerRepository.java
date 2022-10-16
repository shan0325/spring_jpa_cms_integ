package com.spring.cms.repository;

import com.spring.cms.domain.board.BoardManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardManagerRepository extends JpaRepository<BoardManager, Long> {
}
