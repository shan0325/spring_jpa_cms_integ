package com.spring.cms.domain.board;

import com.spring.cms.domain.Category;

import javax.persistence.*;

@Table(name = "TB_NOTICE_BOARD")
@Entity
public class NoticeBoard extends Board {

    @Column(name = "NOTICE_YN", length = 1, nullable = false)
    private Character noticeYn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
}
