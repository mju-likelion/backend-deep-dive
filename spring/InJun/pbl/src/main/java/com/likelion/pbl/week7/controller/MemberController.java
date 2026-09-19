package com.likelion.pbl.week7.controller;

import com.likelion.pbl.week7.domain.role.Lion;
import com.likelion.pbl.week7.domain.role.Role;
import com.likelion.pbl.week7.domain.role.Staff;
import com.likelion.pbl.week7.dto.*;
import com.likelion.pbl.week7.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/lions")
    public ResponseEntity<?> createLion(@RequestBody LionCreateRequest request) {

        Lion lion = memberService.createLion(request);

        if (lion == null) {
            return ResponseEntity.status(409).body("이미 존재하는 이름입니다.");
        }

        return ResponseEntity
                .status(201)
                .body(LionResponse.from(lion));
    }

    @PostMapping("/staffs")
    public ResponseEntity<?> createStaff(@RequestBody StaffCreateRequest request) {

        Staff staff = memberService.createStaff(request);

        if (staff == null) {
            return ResponseEntity.status(409).body("이미 존재하는 이름입니다.");
        }

        return ResponseEntity
                .status(201)
                .body(StaffResponse.from(staff));
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> findMember(@PathVariable String name) {

        Role member = memberService.findMember(name);

        if (member == null) {
            return ResponseEntity.status(404).body("멤버를 찾을 수 없습니다.");
        }

        if (member instanceof Lion lion) {
            return ResponseEntity.ok(LionResponse.from(lion));
        }

        if (member instanceof Staff staff) {
            return ResponseEntity.ok(StaffResponse.from(staff));
        }

        return ResponseEntity.status(404).body("멤버를 찾을 수 없습니다.");
    }

    @PutMapping("/lions/{name}")
    public ResponseEntity<?> updateLion(
            @PathVariable String name,
            @RequestBody LionUpdateRequest request
    ) {

        Lion lion = memberService.updateLion(name, request);

        if (lion == null) {
            return ResponseEntity.status(404).body("멤버를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(LionResponse.from(lion));
    }

    @PutMapping("/staffs/{name}")
    public ResponseEntity<?> updateStaff(
            @PathVariable String name,
            @RequestBody StaffUpdateRequest request
    ) {

        Staff staff = memberService.updateStaff(name, request);

        if (staff == null) {
            return ResponseEntity.status(404).body("멤버를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(StaffResponse.from(staff));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteMember(@PathVariable String name) {

        boolean deleted = memberService.deleteMember(name);

        if (!deleted) {
            return ResponseEntity.status(404).body("멤버를 찾을 수 없습니다.");
        }

        return ResponseEntity.noContent().build();
    }
}