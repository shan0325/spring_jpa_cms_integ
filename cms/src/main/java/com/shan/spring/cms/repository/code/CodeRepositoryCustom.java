package com.shan.spring.cms.repository.code;

import com.shan.spring.cms.repository.code.dto.CodeQueryDto;

import java.util.List;

public interface CodeRepositoryCustom {

    List<CodeQueryDto.AllCodesResponseQuery> findCodes(boolean parentIsNull, List<Long> codeIds);

    List<CodeQueryDto.AllCodesResponseQuery> findCodeGroup(String topCode, String parentCode);
}
