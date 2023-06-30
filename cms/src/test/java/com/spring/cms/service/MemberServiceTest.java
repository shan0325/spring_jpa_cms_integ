package com.spring.cms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.cms.dto.MemberDto;
import com.spring.cms.enums.MemberStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void 회원등록테스트() {
        //given
        MemberDto.Create createMember = MemberDto.Create.builder()
                .name("회원")
                .password("1234")
                .email("member@email.com")
                .hp("01011112222")
                .authorityId(2L)
                .build();

        //when
        MemberDto.Response newMember = memberService.createMember(createMember);

        //then
        assertThat(newMember.getName()).isEqualTo("회원");
    }

    @Test
    @Rollback(value = false)
    public void 회원수정테스트() {
        MemberDto.Update update = MemberDto.Update.builder()
                .id(10L)
                .name("회원1010")
                .password("1111")
                .email("member1010@naver.com")
                .hp("010-1234-5678")
                .memberStatus(MemberStatusEnum.ACTIVITY)
                .authorityIds(Arrays.asList(3L, 4L, 5L))
                .build();

        memberService.updateMember(update);
    }

    @Test
    public void objectMapperTest() throws JsonProcessingException {
        //given
        ObjectMapper objectMapper = new ObjectMapper();

        MemberDto.Create createMember = MemberDto.Create.builder()
                .name("회원1")
                .password("1234")
                .email("member0@email.com")
                .hp("01011112222")
                .build();

        String createMemberJson = objectMapper.writeValueAsString(createMember);
        System.out.println("createMemberJson = " + createMemberJson);

        MemberDto.Create reqCreateMember = objectMapper.readValue(createMemberJson, MemberDto.Create.class);
        System.out.println("reqCreateMember = " + reqCreateMember);
    }

}