package com.shan.spring.cms.domain;

import com.shan.spring.cms.domain.common.BaseEntity;
import com.shan.spring.cms.dto.CodeDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_CODE")
@Entity
public class Code extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Code parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("ord asc, createdDate desc")
    private List<Code> childs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOP_ID")
    private Code top;

    @Column(name = "CODE", nullable = false, length = 30)
    private String code;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LEVEL")
    private Integer level;

    @Column(name = "ORD")
    private Integer ord;

    @Column(name = "USE_YN", length = 1, nullable = false)
    private Character useYn;

    @Builder(access = AccessLevel.PRIVATE)
    public Code(Code parent, List<Code> childs, Code top, String code, String name, String description, Integer level, Integer ord, Character useYn) {
        this.parent = parent;
        this.childs = childs;
        this.top = top;
        this.code = code;
        this.name = name;
        this.description = description;
        this.level = level;
        this.ord = ord;
        this.useYn = useYn;
    }

    //==연관관계 메서드==//
    public void addParentCode(Code parent) {
        this.parent = parent;
        if (parent != null) {
            parent.getChilds().add(this);
        }
    }

    //==생성 메서드==//
    public static Code createCode(CodeDto.Create create, Code parent, Code top, Integer level) {
        Code code = Code.builder()
                .top(top)
                .code(create.getCode())
                .name(create.getName())
                .description(create.getDescription())
                .level(level)
                .ord(create.getOrd())
                .useYn(create.getUseYn())
                .build();

        code.addParentCode(parent);
        return code;
    }

    //==수정 메서드==//
    public void updateCode(CodeDto.Update update) {
        this.name = update.getName();
        this.description = update.getDescription();
        this.ord = update.getOrd();
        this.useYn = update.getUseYn();
    }

}
