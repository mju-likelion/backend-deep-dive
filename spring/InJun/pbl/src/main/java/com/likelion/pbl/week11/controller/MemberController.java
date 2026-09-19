package com.likelion.pbl.week11.controller;

import com.likelion.pbl.week11.domain.Member;
import com.likelion.pbl.week11.dto.LionCreateRequest;
import com.likelion.pbl.week11.dto.LionUpdateRequest;
import com.likelion.pbl.week11.dto.MemberResponse;
import com.likelion.pbl.week11.dto.StaffCreateRequest;
import com.likelion.pbl.week11.dto.StaffUpdateRequest;
import com.likelion.pbl.week11.global.dto.PageResponse;
import com.likelion.pbl.week11.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/lions")
    public ResponseEntity<MemberResponse> createLion(@RequestBody LionCreateRequest request) {
        Member member = memberService.createLion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MemberResponse.from(member));
    }

    @PostMapping("/staffs")
    public ResponseEntity<MemberResponse> createStaff(@RequestBody StaffCreateRequest request) {
        Member member = memberService.createStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MemberResponse.from(member));
    }

    @GetMapping
    public ResponseEntity<PageResponse<MemberResponse>> findAllMembers(
            @RequestParam(required = false) String part,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> members = part != null && !part.isBlank()
                ? memberService.findByPart(part, pageable)
                : memberService.findAllMembers(pageable);

        Page<MemberResponse> responses = members.map(MemberResponse::from);

        return ResponseEntity.ok(PageResponse.from(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        Member member = memberService.findMember(id);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PutMapping("/lions/{id}")
    public ResponseEntity<MemberResponse> updateLion(
            @PathVariable Long id,
            @RequestBody LionUpdateRequest request
    ) {
        Member member = memberService.updateLion(id, request);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PutMapping("/staffs/{id}")
    public ResponseEntity<MemberResponse> updateStaff(
            @PathVariable Long id,
            @RequestBody StaffUpdateRequest request
    ) {
        Member member = memberService.updateStaff(id, request);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
