package com.shan.spring.cms.domain;

import com.shan.spring.cms.domain.common.BaseEntity;
import com.shan.spring.cms.enums.ManagerStatusEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TB_MANAGER")
@Entity
public class Manager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MANAGER_ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "HP")
    private String hp;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private ManagerStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHORITY_ID", nullable = false)
    private Authority authority; // 권한(메뉴)

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<ManagerAuthority> managerAuthorities = new ArrayList<>(); // 리소스 권한(시큐리티)

    @Builder(access = AccessLevel.PRIVATE)
    public Manager(String username, String password, String name, String email, String hp, ManagerStatusEnum status) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.hp = hp;
        this.status = status;
    }

    //==생성 메서드==//
    public static Manager createManager(String username, String password, String name, String email, String hp, ManagerStatusEnum status, ManagerAuthority... managerAuthoritys) {
        Manager manager = Manager.builder()
                .username(username)
                .password(password)
                .name(name)
                .email(email)
                .hp(hp)
                .status(status)
                .build();

        for (ManagerAuthority managerAuthority : managerAuthoritys) {
            managerAuthority.addManagerAuthority(manager);
        }
        return manager;
    }
}
