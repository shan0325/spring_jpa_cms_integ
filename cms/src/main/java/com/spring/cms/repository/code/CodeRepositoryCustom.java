package com.spring.cms.repository.code;

import com.spring.cms.dto.code.CodeQueryDto;

import java.util.List;

public interface CodeRepositoryCustom {

    List<CodeQueryDto.AllCodesResponseQuery> findCodes(boolean parentIsNull, List<Long> codeIds);
}
