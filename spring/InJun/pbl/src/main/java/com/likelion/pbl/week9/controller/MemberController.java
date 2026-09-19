package com.likelion.pbl.week9.controller;

import com.likelion.pbl.week9.domain.Member;
import com.likelion.pbl.week9.dto.*;
import com.likelion.pbl.week9.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/lions")
    public ResponseEntity<?> createLion(@RequestBody LionCreateRequest request) {
        Member member = memberService.createLion(request);

        if (member == null) {
            return ResponseEntity.status(409).body("이미 존재하는 이름입니다.");
        }

        return ResponseEntity.status(201).body(MemberResponse.from(member));
    }

    @PostMapping("/staffs")
    public ResponseEntity<?> createStaff(@RequestBody StaffCreateRequest request) {
        Member member = memberService.createStaff(request);

        if (member == null) {
            return ResponseEntity.status(409).body("이미 존재하는 이름입니다.");
        }

        return ResponseEntity.status(201).body(MemberResponse.from(member));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAllMembers() {
        List<MemberResponse> responses = memberService.findAllMembers()
                .stream()
                .map(MemberResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findMember(@PathVariable Long id) {
        Member member = memberService.findMember(id);

        if (member == null) {
            return ResponseEntity.status(404).body("멤버를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PutMapping("/lions/{id}")
    public ResponseEntity<?> updateLion(
            @PathVariable Long id,
            @RequestBody LionUpdateRequest request
    ) {
        Member member = memberService.updateLion(id, request);

        if (member == null) {
            return ResponseEntity.status(404).body("멤버를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PutMapping("/staffs/{id}")
    public ResponseEntity<?> updateStaff(
            @PathVariable Long id,
            @RequestBody StaffUpdateRequest request
    ) {
        Member member = memberService.updateStaff(id, request);

        if (member == null) {
            return ResponseEntity.status(404).body("멤버를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        boolean deleted = memberService.deleteMember(id);

        if (!deleted) {
            return ResponseEntity.status(404).body("멤버를 찾을 수 없습니다.");
        }

        return ResponseEntity.noContent().build();
    }
}