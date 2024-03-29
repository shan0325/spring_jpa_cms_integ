package com.spring.cms.controller;

import com.spring.cms.dto.member.MemberDto;
import com.spring.cms.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestControllerBase.API_URI_PREFIX)
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<?> searchMembers(Pageable pageable, @RequestParam String search) {
        return ResponseEntity.ok(memberService.searchMembers(pageable, search));
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<?> getMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMember(memberId));
    }

    @PostMapping("/members")
    public ResponseEntity<?> createMember(@RequestBody @Valid MemberDto.Create createMember) {
        return ResponseEntity.ok(memberService.createMember(createMember));
    }

    @PutMapping("/members")
    public ResponseEntity<?> updateMember(@RequestBody @Valid MemberDto.Update updateMember) {
        memberService.updateMember(updateMember);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
