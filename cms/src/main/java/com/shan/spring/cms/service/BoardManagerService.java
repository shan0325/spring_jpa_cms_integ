package com.shan.spring.cms.service;

import com.shan.spring.cms.dto.BoardManagerDto;

public interface BoardManagerService {
    BoardManagerDto.Response createBoardManager(BoardManagerDto.Create create);
}
