package com.shan.spring.cms.domain.board;

import com.shan.spring.cms.domain.common.BaseEntity;

import javax.persistence.*;

@Table(name = "TB_BOARD")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "USE_YN", length = 1, nullable = false)
    private Character useYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_MANAGER_ID")
    private BoardManager boardManager;
}
