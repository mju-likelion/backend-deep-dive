package com.likelion.likelionspring.member.controller;

import com.likelion.likelionspring.member.dto.*;
import com.likelion.likelionspring.global.dto.PageResponse;
import com.likelion.likelionspring.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public PageResponse<MemberResponse> findMembers(@RequestParam(required = false) String part, Pageable pageable) {
        Page<MemberResponse> responses = memberService.findAll(part, pageable)
                .map(MemberResponse::from);
        return new PageResponse<>(responses);
    }

    @GetMapping("/{id}")
    public MemberResponse findMember(@PathVariable Long id) {
        return MemberResponse.from(memberService.findById(id));
    }

    @PutMapping("/lions/{id}")
    public MemberResponse updateLion(@PathVariable Long id, @AuthenticationPrincipal Long memberId,
                                      @RequestBody LionUpdateRequest request) {
        return MemberResponse.from(memberService.updateLion(id, memberId, request));
    }

    @PutMapping("/staffs/{id}")
    public MemberResponse updateStaff(@PathVariable Long id, @AuthenticationPrincipal Long memberId,
                                       @RequestBody StaffUpdateRequest request) {
        return MemberResponse.from(memberService.updateStaff(id, memberId, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable Long id, @AuthenticationPrincipal Long memberId) {
        memberService.deleteMember(id, memberId);
    }
}