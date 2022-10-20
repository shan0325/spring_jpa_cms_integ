package com.spring.cms.service;

import com.spring.cms.dto.BoardManagerDto;

public interface BoardManagerService {
    BoardManagerDto.Response createBoardManager(BoardManagerDto.Create create);
}
