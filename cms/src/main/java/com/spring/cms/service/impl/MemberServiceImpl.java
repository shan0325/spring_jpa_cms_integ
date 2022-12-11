package com.spring.cms.service.impl;

import com.spring.cms.domain.Authority;
import com.spring.cms.domain.Member;
import com.spring.cms.domain.MemberAuthority;
import com.spring.cms.dto.member.MemberDto;
import com.spring.cms.dto.member.MemberQueryDto;
import com.spring.cms.enums.MemberStatusEnum;
import com.spring.cms.exception.AuthorityException;
import com.spring.cms.exception.MemberException;
import com.spring.cms.repository.AuthorityRepository;
import com.spring.cms.repository.member.MemberRepository;
import com.spring.cms.service.MemberService;
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

import static com.spring.cms.exception.AuthorityException.AuthorityExceptionType.NOT_EXIST_AUTHORITY;
import static com.spring.cms.exception.MemberException.MemberExceptionType.ALREADY_EXIST_MEMBER;
import static com.spring.cms.exception.MemberException.MemberExceptionType.NOT_FOUND_MEMBER;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional // 기본이 readOnly = false
    @Override
    public MemberDto.Response createMember(MemberDto.Create createMember) {

        if(memberRepository.existsByEmail(createMember.getEmail())) {
            throw new MemberException(ALREADY_EXIST_MEMBER);
        }

        Authority findAuthority = authorityRepository.findById(createMember.getAuthorityId())
                .orElseThrow(() -> new AuthorityException(NOT_EXIST_AUTHORITY));

        MemberAuthority memberAuthority = MemberAuthority.createMemberAuthority(findAuthority);

        Member member = Member.createMember(createMember.getName(), passwordEncoder.encode(createMember.getPassword()),
                createMember.getEmail(), createMember.getHp(), MemberStatusEnum.ACTIVITY, memberAuthority);

        memberRepository.save(member);

        return modelMapper.map(member, MemberDto.Response.class);
    }

    @Override
    public Page<MemberQueryDto.SearchMembersResponse> searchMembers(Pageable pageable, String search) {
        List<MemberQueryDto.SearchMembersResponse> searchMembers = memberRepository.searchMembers(pageable, search);

        Integer integer = memberRepository.searchMembersTotalCount(pageable, search);

        return new PageImpl<>(searchMembers, pageable, integer);
    }

    @Override
    public MemberDto.Response getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .map(u -> modelMapper.map(u, MemberDto.Response.class))
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }

    @Transactional
    @Override
    public void updateMember(MemberDto.Update updateMember) {
        Member member = memberRepository.findById(updateMember.getId())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));

        String name = updateMember.getName();
        String password = StringUtils.hasText(updateMember.getPassword()) ?
                passwordEncoder.encode(updateMember.getPassword()) : null;
        String email = updateMember.getEmail();
        String hp = updateMember.getHp();
        MemberStatusEnum status = updateMember.getMemberStatus();
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
