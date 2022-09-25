package com.spring.cms.repository.code;

import com.spring.cms.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long>, CodeRepositoryCustom {

    Optional<List<Code>> findByParentOrderByOrd(Code parent);

    Optional<Code> findByParentAndCode(Code parent, String code);

    Integer countByParent(Code parent);
}
