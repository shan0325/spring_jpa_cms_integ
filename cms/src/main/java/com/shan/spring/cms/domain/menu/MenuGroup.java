package com.shan.spring.cms.domain.menu;

import com.shan.spring.cms.domain.Site;
import com.shan.spring.cms.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_MENU_GROUP")
@Entity
public class MenuGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_GROUP_ID")
    private Long id;

    @Column(name = "GROUP_NAME", nullable = false)
    private String groupName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID")
    private Site site;

    @Column(name = "USE_YN", length = 1, nullable = false)
    private Character useYn;
}
