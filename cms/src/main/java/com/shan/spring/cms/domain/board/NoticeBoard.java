package com.shan.spring.cms.domain.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "TB_NOTICE_BOARD")
@Entity
public class NoticeBoard extends Board {

    @Column(name = "NOTICE_YN", length = 1, nullable = false)
    private Character noticeYn;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;
}
