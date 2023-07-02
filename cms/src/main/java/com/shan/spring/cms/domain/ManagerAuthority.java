package com.shan.spring.cms.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_MANAGER_AUTHORITY")
@Entity
public class ManagerAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MANAGER_AUTHORITY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANAGER_ID")
    private Manager manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHORITY_ID")
    private Authority authority;

    public ManagerAuthority(Authority authority) {
        this.authority = authority;
    }

    //==연관관계 메서드==//
    public void addManagerAuthority(Manager manager) {
        if (this.manager != null) {
            this.manager.getManagerAuthorities().remove(this);
        }
        this.manager = manager;
        manager.getManagerAuthorities().add(this);
    }

    //==생성 메서드==//
    public static ManagerAuthority createManagerAuthority(Authority authority) {
        return new ManagerAuthority(authority);
    }

}
