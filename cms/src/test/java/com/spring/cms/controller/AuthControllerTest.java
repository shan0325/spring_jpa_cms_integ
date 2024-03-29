package com.spring.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.cms.dto.ManagerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public static String obtainAdminAccessToken(MockMvc mockMvc) throws Exception {
        return obtainAccessToken(mockMvc, "admin", "1234");
    }

    public static String obtainAccessToken(MockMvc mockMvc, String username, String password) throws Exception {
        ManagerDto.Login login = ManagerDto.Login.builder()
                .username(username)
                .password(password)
                .build();

        ObjectMapper om = new ObjectMapper();
        String loginContent = om.writeValueAsString(login);

        ResultActions result = mockMvc.perform(post(RestControllerBase.API_URI_PREFIX + "/auth/login")
                .content(loginContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String contentAsString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(contentAsString).get("accessToken").toString();
    }

    @Test
    public void login() throws Exception {
        ManagerDto.Login login = ManagerDto.Login.builder()
                .username("admin")
                .password("1234")
                .build();

        ObjectMapper om = new ObjectMapper();
        String loginContent = om.writeValueAsString(login);

        this.mockMvc.perform(post(RestControllerBase.API_URI_PREFIX + "/auth/login")
                .content(loginContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("auth/login",
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("grantType").type(JsonFieldType.STRING).description("grantType"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("accessToken"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("refreshToken"),
                                fieldWithPath("accessTokenExpiresIn").type(JsonFieldType.NUMBER).description("accessTokenExpiresIn")
                        )
                ));
    }

}