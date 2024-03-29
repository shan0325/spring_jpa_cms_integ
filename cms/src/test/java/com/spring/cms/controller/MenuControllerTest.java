package com.spring.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.cms.domain.Contents;
import com.spring.cms.dto.menu.MenuDto;
import com.spring.cms.enums.MenuTypeEnum;
import com.spring.cms.exception.MenuException;
import com.spring.cms.repository.BoardManagerRepository;
import com.spring.cms.repository.ContentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardManagerRepository boardManagerRepository;

    @Autowired
    private ContentsRepository contentsRepository;

    private String accessToken;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        this.accessToken = AuthControllerTest.obtainAdminAccessToken(mockMvc);
    }

    @Test
    @Rollback(value = false)
    public void createMenuBoard() throws Exception {
        MenuDto.Create create = MenuDto.Create.builder()
                .menuGroupId(1L)
                .parentId(1L)
                .topId(1L)
                .level(1)
                .ord(2)
                .name("게시판")
                .description("게시판")
                .useYn('Y')
                .menuType(MenuTypeEnum.MT_BOARD)
                .boardManagerId(1L)
                .build();

        this.mockMvc.perform(post(RestControllerBase.API_URI_PREFIX + "/menus")
                        .header("Authorization", "Bearer " + this.accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(create))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("menus/create",
                        requestFields(
                                fieldWithPath("menuGroupId").type(JsonFieldType.NUMBER).description("메뉴그룹아이디"),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("상위메뉴아이디").optional(),
                                fieldWithPath("topId").type(JsonFieldType.NUMBER).description("최상위메뉴아이디").optional(),
                                fieldWithPath("level").type(JsonFieldType.NUMBER).description("메뉴뎁스"),
                                fieldWithPath("ord").type(JsonFieldType.NUMBER).description("메뉴순서"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴명"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("메뉴설명").optional(),
                                fieldWithPath("useYn").type(JsonFieldType.STRING).description("사용유무"),
                                fieldWithPath("menuType").type(JsonFieldType.STRING).description("메뉴타입"),
                                fieldWithPath("boardManagerId").type(JsonFieldType.NUMBER).description("게시판관리아이디").optional(),
                                fieldWithPath("link").type(JsonFieldType.STRING).description("바로가기링크").optional(),
                                fieldWithPath("linkTarget").type(JsonFieldType.STRING).description("바로가기링크타겟").optional(),
                                fieldWithPath("contentsId").type(JsonFieldType.NUMBER).description("컨텐츠아이디").optional()
                        )
                ));
    }

    @Test
    @Rollback(value = false)
    public void createMenuLink() throws Exception {
        MenuDto.Create create = MenuDto.Create.builder()
                .menuGroupId(1L)
                .parentId(null)
                .topId(null)
                .level(0)
                .ord(1)
                .name("바로가기")
                .description("바로가기")
                .useYn('Y')
                .menuType(MenuTypeEnum.MT_LINK)
                .link("https://www.naver.com")
                .linkTarget("BLANK")
                .build();

        this.mockMvc.perform(post(RestControllerBase.API_URI_PREFIX + "/menus")
                .header("Authorization", "Bearer " + this.accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(create))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Rollback(value = false)
    public void createMenuMenu() throws Exception {
        MenuDto.Create create = MenuDto.Create.builder()
                .menuGroupId(1L)
                .parentId(null)
                .topId(null)
                .level(0)
                .ord(1)
                .name("메뉴")
                .description("메뉴")
                .useYn('Y')
                .menuType(MenuTypeEnum.MT_MENU)
                .build();

        this.mockMvc.perform(post(RestControllerBase.API_URI_PREFIX + "/menus")
                .header("Authorization", "Bearer " + this.accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(create))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Rollback(value = false)
    public void createMenuContents() throws Exception {
        Contents contents = Contents.createContents("뉴스컨텐츠", "오늘의뉴스~~", 'Y');
        contentsRepository.save(contents);

        MenuDto.Create create = MenuDto.Create.builder()
                .menuGroupId(1L)
                .parentId(null)
                .topId(null)
                .level(0)
                .ord(1)
                .name("컨텐츠")
                .description("컨텐츠")
                .useYn('Y')
                .menuType(MenuTypeEnum.MT_CONTENTS)
                .contentsId(contents.getId())
                .build();

        this.mockMvc.perform(post(RestControllerBase.API_URI_PREFIX + "/menus")
                .header("Authorization", "Bearer " + this.accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(create))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getMenus() throws Exception {
        this.mockMvc.perform(get(RestControllerBase.API_URI_PREFIX + "/menus")
                .header("Authorization", "Bearer " + this.accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("menus/getMenus",
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("메뉴아이디"),
                                fieldWithPath("[].menuGroupId").type(JsonFieldType.NUMBER).description("메뉴그룹아이디"),
                                fieldWithPath("[].parentId").type(JsonFieldType.NUMBER).description("상위메뉴아이디").optional(),
                                fieldWithPath("[].topId").type(JsonFieldType.NUMBER).description("최상위메뉴아이디").optional(),
                                fieldWithPath("[].level").type(JsonFieldType.NUMBER).description("메뉴뎁스"),
                                fieldWithPath("[].ord").type(JsonFieldType.NUMBER).description("메뉴순서"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("메뉴명"),
                                subsectionWithPath("[].childMenus").type(JsonFieldType.ARRAY).description("하위메뉴").optional()
                        )
                ));
    }

    @Test
    public void getMenusOpti() throws Exception {
        this.mockMvc.perform(get(RestControllerBase.API_URI_PREFIX + "/menus/opti")
                .header("Authorization", "Bearer " + this.accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getMenuDetail() throws Exception {
        this.mockMvc.perform(get(RestControllerBase.API_URI_PREFIX + "/menus/{menuId}", 1L)
                .header("Authorization", "Bearer " + this.accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
        .andDo(document("menus/menuDetail",
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("메뉴아이디"),
                        fieldWithPath("menuGroupId").type(JsonFieldType.NUMBER).description("메뉴그룹아이디"),
                        fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("상위메뉴아이디").optional(),
                        fieldWithPath("topId").type(JsonFieldType.NUMBER).description("최상위메뉴아이디").optional(),
                        fieldWithPath("level").type(JsonFieldType.NUMBER).description("메뉴뎁스"),
                        fieldWithPath("ord").type(JsonFieldType.NUMBER).description("메뉴순서"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴명"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("메뉴설명").optional(),
                        fieldWithPath("useYn").type(JsonFieldType.STRING).description("사용유무"),
                        fieldWithPath("menuType").type(JsonFieldType.STRING).description("메뉴타입"),
                        fieldWithPath("boardManagerId").type(JsonFieldType.NUMBER).description("게시판관리아이디").optional(),
                        fieldWithPath("link").type(JsonFieldType.STRING).description("바로가기링크").optional(),
                        fieldWithPath("linkTarget").type(JsonFieldType.STRING).description("바로가기링크타겟").optional(),
                        fieldWithPath("contentsId").type(JsonFieldType.NUMBER).description("컨텐츠아이디").optional(),
                        fieldWithPath("createdDate").type(JsonFieldType.STRING).description("생성일시"),
                        fieldWithPath("lastModifiedDate").type(JsonFieldType.STRING).description("수정일시")
                )));
    }

    @Test
    @Rollback(value = false)
    public void updateMenu() throws Exception {
        MenuDto.Update update = MenuDto.Update.builder()
                .menuGroupId(1L)
                .ord(1)
                .name("서울소식_update")
                .description("서울소식_update")
                .useYn('N')
                .menuType(MenuTypeEnum.MT_BOARD)
                .boardManagerId(1L)
                .build();

        this.mockMvc.perform(put(RestControllerBase.API_URI_PREFIX + "/menus/{menuId}", 1L)
                .header("Authorization", "Bearer " + this.accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(update))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("menus/update",
                        requestFields(
                                fieldWithPath("menuGroupId").type(JsonFieldType.NUMBER).description("메뉴그룹아이디"),
                                fieldWithPath("ord").type(JsonFieldType.NUMBER).description("메뉴순서"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴명"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("메뉴설명").optional(),
                                fieldWithPath("useYn").type(JsonFieldType.STRING).description("사용유무"),
                                fieldWithPath("menuType").type(JsonFieldType.STRING).description("메뉴타입"),
                                fieldWithPath("boardManagerId").type(JsonFieldType.NUMBER).description("게시판관리아이디").optional(),
                                fieldWithPath("link").type(JsonFieldType.STRING).description("바로가기링크").optional(),
                                fieldWithPath("linkTarget").type(JsonFieldType.STRING).description("바로가기링크타겟").optional(),
                                fieldWithPath("contentsId").type(JsonFieldType.NUMBER).description("컨텐츠아이디").optional(),
                                fieldWithPath("materialIcon").type(JsonFieldType.STRING).description("material 아이콘").optional()
                        )
                ));
    }

    @Test
    public void deleteMenu() throws Exception {
        this.mockMvc.perform(delete(RestControllerBase.API_URI_PREFIX + "/menus/{menuId}", 1L)
                        .header("Authorization", "Bearer " + this.accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("menus/delete"));
    }

}