package com.spring.cms.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ManagerAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
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
