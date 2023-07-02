package com.shan.spring.cms.service.impl;

import com.shan.spring.cms.dto.BoardManagerDto;
import com.shan.spring.cms.enums.BoardTypeEnum;
import com.shan.spring.cms.repository.BoardManagerRepository;
import com.shan.spring.cms.domain.board.BoardManager;
import com.shan.spring.cms.service.BoardManagerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardManagerServiceImpl implements BoardManagerService {

    private final BoardManagerRepository boardManagerRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public BoardManagerDto.Response createBoardManager(BoardManagerDto.Create create) {
        BoardManager boardManager = BoardManager.createBoardManager(create.getName(), create.getDescription(), BoardTypeEnum.valueOf(create.getBoardType())
                , create.getBoardUseYn(), create.getCommentUseYn());

        boardManagerRepository.save(boardManager);

        return modelMapper.map(boardManager, BoardManagerDto.Response.class);
    }
}
