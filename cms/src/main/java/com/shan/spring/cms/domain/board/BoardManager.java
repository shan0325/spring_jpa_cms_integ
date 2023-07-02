package com.shan.spring.cms.domain.board;

import com.shan.spring.cms.domain.common.BaseEntity;
import com.shan.spring.cms.enums.BoardTypeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Table(name = "TB_BOARD_MANAGER")
@Entity
public class BoardManager extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_MANAGER_ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "BOARD_TYPE", nullable = false)
    private BoardTypeEnum boardType;

    @Column(name = "BOARD_USE_YN", length = 1)
    private Character boardUseYn;

    @Column(name = "COMMENT_USE_YN", length = 1)
    private Character commentUseYn;

    @Builder(access = AccessLevel.PRIVATE)
    public BoardManager(String name, String description, BoardTypeEnum boardType, Character boardUseYn, Character commentUseYn) {
        this.name = name;
        this.description = description;
        this.boardType = boardType;
        this.boardUseYn = boardUseYn;
        this.commentUseYn = commentUseYn;
    }

    //==생성 메서드==//
    public static BoardManager createBoardManager(String name, String description, BoardTypeEnum boardType, Character boardUseYn, Character commentUseYn) {
        return BoardManager.builder()
                .name(name)
                .description(description)
                .boardType(boardType)
                .boardUseYn(boardUseYn)
                .commentUseYn(commentUseYn)
                .build();
    }
}
