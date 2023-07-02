package com.shan.spring.cms.repository;

import com.shan.spring.cms.domain.board.BoardManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardManagerRepository extends JpaRepository<BoardManager, Long> {
}
