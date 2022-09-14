package com.spring.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.cms.dto.code.CodeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
class CodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String accessToken;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        this.accessToken = AuthControllerTest.obtainAdminAccessToken(mockMvc);
    }

    @Test
    public void createCode() throws Exception {
        CodeDto.Create create = CodeDto.Create.builder()
                .parentId(null)
                .topId(null)
                .code("A_GROUP")
                .name("a그룹")
                .description("a그룹")
                .ord(1)
                .useYn('Y')
                .build();

        this.mockMvc.perform(post(RestControllerBase.API_URI_PREFIX + "/codes")
                        .header("Authorization", "Bearer " + this.accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getCodes() throws Exception{
        this.mockMvc.perform(get(RestControllerBase.API_URI_PREFIX + "/codes")
                        .header("Authorization", "Bearer " + this.accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateCode() throws Exception {
        CodeDto.Update update = CodeDto.Update.builder()
                .name("그룹에이")
                .description("그룹에이")
                .ord(1)
                .useYn('Y')
                .build();

        this.mockMvc.perform(put(RestControllerBase.API_URI_PREFIX + "/codes/1")
                        .header("Authorization", "Bearer " + this.accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCode() throws Exception {
        this.mockMvc.perform(delete(RestControllerBase.API_URI_PREFIX + "/codes/7")
                        .header("Authorization", "Bearer " + this.accessToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

}