package com.shan.spring.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_MEMBER_AUTHORITY")
@Entity
public class MemberAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_AUTHORITY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHORITY_ID")
    private Authority authority;

    public MemberAuthority(Authority authority) {
        this.authority = authority;
    }
    public MemberAuthority(Member member, Authority authority) {
        this.member = member;
        this.authority = authority;
    }

    //==연관관계 메서드==//
    public void addMemberAuthority(Member member) {
        if (this.member != null) {
            this.member.getMemberAuthorities().remove(this);
        }
        this.member = member;
        member.getMemberAuthorities().add(this);
    }

    //==생성 메서드==//
    public static MemberAuthority createMemberAuthority(Authority authority) {
        return new MemberAuthority(authority);
    }
}
