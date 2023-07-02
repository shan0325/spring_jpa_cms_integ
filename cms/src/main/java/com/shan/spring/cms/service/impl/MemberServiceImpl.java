package com.shan.spring.cms.service.impl;

import com.shan.spring.cms.domain.MemberAuthority;
import com.shan.spring.cms.dto.MemberDto;
import com.shan.spring.cms.enums.MemberStatusEnum;
import com.shan.spring.cms.exception.AuthorityException;
import com.shan.spring.cms.exception.MemberException;
import com.shan.spring.cms.repository.AuthorityRepository;
import com.shan.spring.cms.repository.member.MemberRepository;
import com.shan.spring.cms.repository.member.dto.MemberQueryDto;
import com.shan.spring.cms.domain.Authority;
import com.shan.spring.cms.domain.Member;
import com.shan.spring.cms.service.MemberService;
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
            throw new MemberException(MemberException.MemberExceptionType.ALREADY_EXIST_MEMBER);
        }

        Authority findAuthority = authorityRepository.findById(createMember.getAuthorityId())
                .orElseThrow(() -> new AuthorityException(AuthorityException.AuthorityExceptionType.NOT_EXIST_AUTHORITY));

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
                .orElseThrow(() -> new MemberException(MemberException.MemberExceptionType.NOT_FOUND_MEMBER));
    }

    @Transactional
    @Override
    public void updateMember(MemberDto.Update updateMember) {
        Member member = memberRepository.findById(updateMember.getId())
                .orElseThrow(() -> new MemberException(MemberException.MemberExceptionType.NOT_FOUND_MEMBER));

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
                    .orElseThrow(() -> new AuthorityException(AuthorityException.AuthorityExceptionType.NOT_EXIST_AUTHORITY));
            memberAuthorities.add(MemberAuthority.createMemberAuthority(findAuthority));
        });

        member.getMemberAuthorities().clear();
        member.updateMember(name, password, email, hp, status, memberAuthorities);
    }
}
