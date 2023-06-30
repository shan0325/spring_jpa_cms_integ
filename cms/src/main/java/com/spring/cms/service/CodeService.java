package com.spring.cms.service;

import com.spring.cms.dto.CodeDto;
import com.spring.cms.repository.code.dto.CodeQueryDto;

import java.util.List;

public interface CodeService {
    void createCode(CodeDto.Create create);
    List<CodeDto.AllCodesResponse> getAllCodes();
    List<CodeQueryDto.AllCodesResponseQuery> getAllCodesOpti();
    void updateCode(Long codeId, CodeDto.Update update);
    void deleteCode(Long codeId);
    CodeDto.CodeResponse getCode(Long codeId);
    List<CodeQueryDto.AllCodesResponseQuery> getTopCodeGroup(String topCode);
    List<CodeQueryDto.AllCodesResponseQuery> getParentCodeGroup(String topCode, String parentCode);
}
