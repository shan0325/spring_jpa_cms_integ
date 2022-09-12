package com.spring.cms.repository.code;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.cms.dto.code.CodeQueryDto;
import com.spring.cms.dto.code.QCodeQueryDto_AllCodesResponseQuery;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.spring.cms.domain.QCode.code1;

@RequiredArgsConstructor
public class CodeRepositoryImpl implements CodeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CodeQueryDto.AllCodesResponseQuery> findCodes(boolean parentIsNull, List<Long> codeIds) {
        return queryFactory
                .select(new QCodeQueryDto_AllCodesResponseQuery(
                        code1.id,
                        code1.parent.id,
                        code1.top.id,
                        code1.code,
                        code1.name,
                        code1.description,
                        code1.level,
                        code1.ord,
                        code1.useYn
                ))
                .from(code1)
                .where(
                        parentIsNull(parentIsNull),
                        parentIdIn(codeIds)
                )
                .orderBy(code1.ord.asc(), code1.createdDate.desc())
                .fetch();
    }

    private BooleanExpression parentIsNull(boolean parentIsNull) {
        return parentIsNull == true ? code1.parent.isNull() : null;
    }

    private BooleanExpression parentIdIn(List<Long> codeIds) {
        return codeIds != null ? code1.parent.id.in(codeIds) : null;
    }
}
