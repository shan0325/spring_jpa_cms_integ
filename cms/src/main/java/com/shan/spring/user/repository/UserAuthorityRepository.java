package com.shan.spring.user.repository;

import com.shan.spring.user.domain.Authority;
import com.shan.spring.user.enums.AuthorityTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByAuthority(String authority);

    Optional<List<Authority>> findByAuthorityType(AuthorityTypeEnum authorityType);

}
