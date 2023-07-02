package com.spring.cms.domain;

import com.spring.cms.domain.common.BaseEntity;
import com.spring.cms.enums.MemberStatusEnum;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_MEMBER")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "HP")
    private String hp;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private MemberStatusEnum status;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberAuthority> memberAuthorities = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Member(String name, String password, String email, String hp, MemberStatusEnum status) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.hp = hp;
        this.status = status;
    }

    //==생성 메서드==//
    public static Member createMember(String name, String password, String email, String hp, MemberStatusEnum status, MemberAuthority... memberAuthoritys) {
        Member member = Member.builder()
                .name(name)
                .password(password)
                .email(email)
                .hp(hp)
                .status(status)
                .build();

        for (MemberAuthority memberAuthority : memberAuthoritys) {
            memberAuthority.addMemberAuthority(member);
        }
        return member;
    }

    //== 수정 메서드==//
    public void updateMember(String name, String password, String email, String hp, MemberStatusEnum status, List<MemberAuthority> memberAuthorities) {
        this.name = name;
        if (StringUtils.hasText(password)) {
            this.password = password;
        }
        this.email = email;
        this.hp = hp;
        this.status = status;

        for (MemberAuthority memberAuthority : memberAuthorities) {
            memberAuthority.addMemberAuthority(this);
        }
    }
}
