package com.shan.spring.cms.service.impl;

import com.shan.spring.cms.dto.CodeDto;
import com.shan.spring.cms.exception.CodeException;
import com.shan.spring.cms.repository.code.CodeRepository;
import com.shan.spring.cms.repository.code.dto.CodeQueryDto;
import com.shan.spring.cms.domain.Code;
import com.shan.spring.cms.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CodeServiceImpl implements CodeService {

    private final CodeRepository codeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void createCode(CodeDto.Create create) {
        Code parentCode = null;
        Code topCode = null;
        Integer level = 1;

        Long parentId = create.getParentId();
        Long topId = create.getTopId();
        if (parentId != null && topId != null) {
            parentCode = codeRepository.findById(parentId)
                    .orElseThrow(() -> new CodeException(CodeException.CodeExceptionType.NOT_FOUND_PARENT_CODE));

            topCode = codeRepository.findById(topId)
                    .orElseThrow(() -> new CodeException(CodeException.CodeExceptionType.NOT_FOUND_TOP_CODE));

            level = parentCode.getLevel() + 1;
        }

        Optional<Code> codeOptional = codeRepository.findByParentAndCode(parentCode, create.getCode());
        if (codeOptional.isPresent()) {
            throw new CodeException(CodeException.CodeExceptionType.DUPLICATED_CODE);
        }

        Code code = Code.createCode(create, parentCode, topCode, level);
        codeRepository.save(code);
    }

    @Override
    public List<CodeDto.AllCodesResponse> getAllCodes() {
        return codeRepository.findByParentOrderByOrd(null)
                .orElseThrow(() -> new CodeException(CodeException.CodeExceptionType.NOT_FOUND_CODE))
                .stream()
                .map(CodeDto.AllCodesResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CodeQueryDto.AllCodesResponseQuery> getAllCodesOpti() {
        List<CodeQueryDto.AllCodesResponseQuery> codes = codeRepository.findCodes(true, null);
        setChildCodesByRecursive(codes);
        return codes;
    }

    private void setChildCodesByRecursive(List<CodeQueryDto.AllCodesResponseQuery> codes) {
        if (codes == null || codes.isEmpty()) {
            return;
        }

        List<CodeQueryDto.AllCodesResponseQuery> childAllCodes = new ArrayList<>();

        Map<Long, List<CodeQueryDto.AllCodesResponseQuery>> codeChildsMap = findCodeChildsMap(toCodeIds(codes));
        codes.forEach(c -> {
            List<CodeQueryDto.AllCodesResponseQuery> childCodes = codeChildsMap.get(c.getId());
            c.setChildCodes(childCodes);
            if (childCodes != null) {
                childAllCodes.addAll(childCodes);
            }
        });
        setChildCodesByRecursive(childAllCodes);
    }

    private Map<Long, List<CodeQueryDto.AllCodesResponseQuery>> findCodeChildsMap(List<Long> codeIds) {
        return codeRepository.findCodes(false, codeIds).stream()
                .collect(Collectors.groupingBy(allCodesResponseQuery -> allCodesResponseQuery.getParentId()));
    }

    private List<Long> toCodeIds(List<CodeQueryDto.AllCodesResponseQuery> codes) {
        return codes.stream()
                .map(c -> c.getId())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateCode(Long codeId, CodeDto.Update update) {
        Code findCode = codeRepository.findById(codeId)
                .orElseThrow(() -> new CodeException(CodeException.CodeExceptionType.NOT_FOUND_CODE));

        findCode.updateCode(update);
    }

    @Transactional
    @Override
    public void deleteCode(Long codeId) {
        Code findCode = codeRepository.findById(codeId)
                .orElseThrow(() -> new CodeException(CodeException.CodeExceptionType.NOT_FOUND_CODE));

        Integer childCount = codeRepository.countByParent(findCode);
        if (childCount > 0) {
            throw new CodeException(CodeException.CodeExceptionType.CHILD_CODE_EXISTS_CANNOT_DELETE);
        }

        codeRepository.delete(findCode);
    }

    @Override
    public CodeDto.CodeResponse getCode(Long codeId) {
        Code findCode = codeRepository.findById(codeId)
                .orElseThrow(() -> new CodeException(CodeException.CodeExceptionType.NOT_FOUND_CODE));

        return modelMapper.map(findCode, CodeDto.CodeResponse.class);
    }

    @Override
    public List<CodeQueryDto.AllCodesResponseQuery> getTopCodeGroup(String topCode) {
        Code findTopCode = codeRepository.findTopCodeByCode(topCode)
                .orElseThrow(() -> new CodeException(CodeException.CodeExceptionType.NOT_FOUND_CODE));

        List<CodeQueryDto.AllCodesResponseQuery> codes = codeRepository.findCodes(false, Arrays.asList(findTopCode.getId()));
        setChildCodesByRecursive(codes);
        return codes;
    }

    @Override
    public List<CodeQueryDto.AllCodesResponseQuery> getParentCodeGroup(String topCode, String parentCode) {
        return codeRepository.findCodeGroup(topCode, parentCode);
    }
}
