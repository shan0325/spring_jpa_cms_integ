package com.spring.cms.domain;

import com.spring.cms.domain.common.BaseEntity;
import com.spring.cms.dto.CodeDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Code extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Code parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("ord asc, createdDate desc")
    private List<Code> childs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top_id")
    private Code top;

    @Column(nullable = false, length = 30)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;

    private String description;

    private Integer level;

    private Integer ord;

    @Column(length = 1, nullable = false)
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
