package com.shan.spring.cms.repository.code;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shan.spring.cms.domain.QCode;
import com.shan.spring.cms.repository.code.dto.CodeQueryDto;
import com.shan.spring.cms.repository.code.dto.QCodeQueryDto_AllCodesResponseQuery;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CodeRepositoryImpl implements CodeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CodeQueryDto.AllCodesResponseQuery> findCodes(boolean parentIsNull, List<Long> codeIds) {
        return queryFactory
                .select(new QCodeQueryDto_AllCodesResponseQuery(
                        QCode.code1.id,
                        QCode.code1.parent.id,
                        QCode.code1.top.id,
                        QCode.code1.code,
                        QCode.code1.name,
                        QCode.code1.description,
                        QCode.code1.level,
                        QCode.code1.ord,
                        QCode.code1.useYn
                ))
                .from(QCode.code1)
                .where(
                        parentIsNull(parentIsNull),
                        parentIdIn(codeIds)
                )
                .orderBy(QCode.code1.ord.asc(), QCode.code1.createdDate.desc())
                .fetch();
    }

    private BooleanExpression parentIsNull(boolean parentIsNull) {
        return parentIsNull ? QCode.code1.parent.isNull() : null;
    }

    private BooleanExpression parentIdIn(List<Long> codeIds) {
        return codeIds != null ? QCode.code1.parent.id.in(codeIds) : null;
    }

    @Override
    public List<CodeQueryDto.AllCodesResponseQuery> findCodeGroup(String topCode, String parentCode) {
        QCode joinCode1 = new QCode("joinCode1");
        QCode joinCode2 = new QCode("joinCode2");

        return queryFactory
                .select(new QCodeQueryDto_AllCodesResponseQuery(
                        joinCode2.id,
                        joinCode2.parent.id,
                        joinCode2.top.id,
                        joinCode2.code,
                        joinCode2.name,
                        joinCode2.description,
                        joinCode2.level,
                        joinCode2.ord,
                        joinCode2.useYn
                ))
                .from(QCode.code1)
                    .innerJoin(joinCode1)
                        .on(
                            QCode.code1.eq(joinCode1.parent),
                            joinCode1.useYn.eq('Y')
                        )
                    .innerJoin(joinCode2)
                        .on(
                            joinCode1.eq(joinCode2.parent),
                            joinCode2.useYn.eq('Y')
                        )
                .where(
                    QCode.code1.code.eq(topCode),
                    joinCode1.code.eq(parentCode)
                )
                .orderBy(joinCode2.ord.asc(), joinCode2.createdDate.desc())
                .fetch();
    }
}
