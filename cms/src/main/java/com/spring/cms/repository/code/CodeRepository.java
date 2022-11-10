package com.spring.cms.repository.code;

import com.spring.cms.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long>, CodeRepositoryCustom {

    Optional<List<Code>> findByParentOrderByOrd(Code parent);

    Optional<Code> findByParentAndCode(Code parent, String code);

    Integer countByParent(Code parent);

    @Query("select c " +
            "from Code c " +
            "where c.code = :topCode " +
            "and c.top is null " +
            "and c.useYn = 'Y'")
    Optional<Code> findTopCodeByCode(@Param("topCode") String topCode);

}
