package com.spring.cms.service;

import com.spring.cms.dto.CodeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CodeServiceTest {

    @Autowired
    private CodeService codeService;

    @Test
    public void createCode() {
        CodeDto.Create create = CodeDto.Create.builder()
                .parentId(null)
                .topId(null)
                .code("A_GROUP")
                .name("a그룹")
                .description("a그룹")
                .ord(1)
                .useYn('Y')
                .build();

        codeService.createCode(create);
    }

}