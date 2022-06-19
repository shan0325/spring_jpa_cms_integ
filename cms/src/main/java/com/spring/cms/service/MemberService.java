package com.spring.cms.service;

import com.spring.cms.domain.Authority;
import com.spring.cms.domain.Member;
import com.spring.cms.domain.MemberAuthority;
import com.spring.cms.dto.MemberDto;
import com.spring.cms.enums.MemberStatus;
import com.spring.cms.exception.AuthorityException;
import com.spring.cms.exception.MemberException;
import com.spring.cms.repository.AuthorityRepository;
import com.spring.cms.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.spring.cms.exception.AuthorityException.AuthorityExceptionType.NOT_EXIST_AUTHORITY;
import static com.spring.cms.exception.MemberException.MemberExceptionType.ALREADY_EXIST_MEMBER;
import static com.spring.cms.exception.MemberException.MemberExceptionType.NOT_FOUND_MEMBER;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional // 기본이 readOnly = false
    public MemberDto.Response createMember(MemberDto.Create createMember) {

        if(memberRepository.existsByEmail(createMember.getEmail())) {
            throw new MemberException(ALREADY_EXIST_MEMBER);
        }

        Authority findAuthority = authorityRepository.findById(createMember.getAuthorityId())
                .orElseThrow(() -> new AuthorityException(NOT_EXIST_AUTHORITY));

        MemberAuthority memberAuthority = MemberAuthority.createMemberAuthority(findAuthority);

        Member member = Member.createMember(createMember.getName(), passwordEncoder.encode(createMember.getPassword()),
                createMember.getEmail(), createMember.getHp(), MemberStatus.ACTIVITY, memberAuthority);

        memberRepository.save(member);

        return modelMapper.map(member, MemberDto.Response.class);
    }

    public Page<MemberDto.MemberResponse> searchMembers(Pageable pageable, String search) {
        List<MemberDto.MemberResponse> searchMembers = memberRepository.searchMembers(pageable, search);

        Integer integer = memberRepository.searchMembersTotalCount(pageable, search);

        return new PageImpl<>(searchMembers, pageable, integer);
    }

    public MemberDto.Response getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .map(u -> modelMapper.map(u, MemberDto.Response.class))
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }

    @Transactional
    public void updateMember(MemberDto.Update updateMember) {
        Member member = memberRepository.findById(updateMember.getId())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));

        String name = updateMember.getName();
        String password = StringUtils.hasText(updateMember.getPassword()) ?
                passwordEncoder.encode(updateMember.getPassword()) : null;
        String email = updateMember.getEmail();
        String hp = updateMember.getHp();
        MemberStatus status = updateMember.getMemberStatus();
        List<Long> authorityIds = updateMember.getAuthorityIds();

        List<MemberAuthority> memberAuthorities = new ArrayList<>();
        authorityIds.forEach(authorityId -> {
            Authority findAuthority = authorityRepository.findById(authorityId)
                    .orElseThrow(() -> new AuthorityException(NOT_EXIST_AUTHORITY));
            memberAuthorities.add(MemberAuthority.createMemberAuthority(findAuthority));
        });

        member.getMemberAuthorities().clear();
        member.updateMember(name, password, email, hp, status, memberAuthorities);
    }
}
