package com.spring.cms.domain;

import com.spring.cms.domain.common.BaseEntity;
import com.spring.cms.enums.ManagerStatus;
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
public class Manager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String email;

    private String hp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ManagerStatus status;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<ManagerAuthority> managerAuthorities = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Manager(String username, String password, String name, String email, String hp, ManagerStatus status) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.hp = hp;
        this.status = status;
    }

    //==생성 메서드==//
    public static Manager createManager(String username, String password, String name, String email, String hp, ManagerStatus status, ManagerAuthority... managerAuthoritys) {
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
