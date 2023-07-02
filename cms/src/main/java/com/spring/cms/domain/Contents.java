package com.spring.cms.domain;

import com.spring.cms.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Table(name = "TB_CONTENTS")
@Entity
public class Contents extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTENTS_ID")
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "USE_YN", length = 1, nullable = false)
    private Character useYn;

    @Builder(access = AccessLevel.PRIVATE)
    public Contents(String title, String content, Character useYn) {
        this.title = title;
        this.content = content;
        this.useYn = useYn;
    }

    //==생성 메서드==//
    public static Contents createContents(String title, String content, Character useYn) {
        return Contents.builder()
                .title(title)
                .content(content)
                .useYn(useYn)
                .build();
    }
}
