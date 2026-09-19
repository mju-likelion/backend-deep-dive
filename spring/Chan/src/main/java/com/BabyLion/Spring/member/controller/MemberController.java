package com.BabyLion.Spring.member.controller;

import com.BabyLion.Spring.global.dto.PageResponse;
import com.BabyLion.Spring.member.domain.Member;
import com.BabyLion.Spring.member.dto.*;
import com.BabyLion.Spring.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Member", description = "멤버 관리 API")
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "Lion 등록", description = "아기사자를 등록합니다.")
    @PostMapping("/lions")
    public ResponseEntity<MemberResponse> createLion(@Valid @RequestBody LionCreateRequest dto) {        Member member = memberService.createLion(dto);
        MemberResponse response = MemberResponse.from(member);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Staff 등록", description = "운영진을 등록합니다.")
    @PostMapping("/staffs")
    public ResponseEntity<MemberResponse> createStaff(@Valid @RequestBody StaffCreateRequest dto) {
        Member member = memberService.createStaff(dto);
        MemberResponse response = MemberResponse.from(member);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "단일 조회", description = "이름으로 멤버를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
        Member member = memberService.searchById(id);
        return ResponseEntity.status(200).body(MemberResponse.from(member));
    }


    @Operation(summary = "Lion 수정", description = "아기사자 정보를 수정합니다.")
    @PutMapping("/lions/{id}")
    public ResponseEntity<MemberResponse> updateLion(@PathVariable Long id, @RequestBody LionUpdateRequest dto) {
        Member member = memberService.updateLion(id, dto);
        return ResponseEntity.status(200).body(MemberResponse.from(member));
    }

    @Operation(summary = "Staff 수정", description = "운영진 정보를 수정합니다.")
    @PutMapping("/staffs/{id}")
    public ResponseEntity<MemberResponse> updateStaff(@PathVariable Long id, @RequestBody StaffUpdateRequest dto) {
        Member member = memberService.updateStaff(id, dto);
        return ResponseEntity.status(200).body(MemberResponse.from(member));
    }

    @Operation(summary = "멤버 삭제", description = "이름으로 멤버를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "전체 조회", description = "모든 멤버를 조회합니다.")
    @GetMapping("")
    public ResponseEntity<?> getAllMembers(@RequestParam(required = false) String part, Pageable pageable) {

        if (part == null) {
            PageResponse<MemberResponse> result = memberService.getAllMembers(pageable);
            return ResponseEntity.status(200).body(result);
        } else {
            List<Member> members;
            members = memberService.findByPart(part);
            List<MemberResponse> response = members.stream()
                    .map(MemberResponse::from)
                    .toList();
            return ResponseEntity.status(200).body(response);
        }
    }
}
