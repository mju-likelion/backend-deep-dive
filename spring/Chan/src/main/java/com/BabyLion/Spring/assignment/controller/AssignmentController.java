package com.BabyLion.Spring.assignment.controller;

import com.BabyLion.Spring.assignment.domain.Assignment;
import com.BabyLion.Spring.assignment.dto.AssignmentCreateRequest;
import com.BabyLion.Spring.assignment.dto.AssignmentResponse;
import com.BabyLion.Spring.assignment.dto.AssignmentUpdateRequest;
import com.BabyLion.Spring.assignment.service.AssignmentService;
import com.BabyLion.Spring.global.dto.PageResponse;
import com.BabyLion.Spring.member.domain.Member;
import com.BabyLion.Spring.member.dto.LionCreateRequest;
import com.BabyLion.Spring.member.dto.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Assignments", description = "과제 관리 API")
@RestController
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Operation(summary = "과제 등록", description = "과제를 등록합니다.")
    @PostMapping("/members/{memberId}/assignments")
    public ResponseEntity<AssignmentResponse> createAssignment(@PathVariable Long memberId,@RequestBody AssignmentCreateRequest dto) {
        Assignment assignment = assignmentService.createAssignment(memberId, dto);
        AssignmentResponse response = AssignmentResponse.from(assignment);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "멤버별 과제 목록 조회", description = "멤버별 과제목록를 조회합니다.")
    @GetMapping("/members/{memberId}/assignments")
    public ResponseEntity<List<AssignmentResponse>> getAssignmentsByMember(@PathVariable Long memberId) {
        List<Assignment> assignments = assignmentService.memberAssignment(memberId);
        List<AssignmentResponse> response = assignments.stream()
                .map(AssignmentResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "과제 단건 조회", description = "과제를 단건 조회합니다.")
    @GetMapping("/assignments/{id}")
    public ResponseEntity<AssignmentResponse> getAssignment(@PathVariable Long id) {
        Assignment assignment = assignmentService.searchAssignment(id);
        AssignmentResponse response = AssignmentResponse.from(assignment);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 과제 조회", description = "전체 과제들을 조회합니다.")
    @GetMapping("/assignments")
    public ResponseEntity<PageResponse<AssignmentResponse>> getAllAssignment(Pageable pageable){
        PageResponse<AssignmentResponse> result = assignmentService.getAllAssignments(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "과제 수정", description = "과제를 수정합니다.")
    @PutMapping("/assignments/{id}")
    public ResponseEntity<AssignmentResponse> updateAssignment(@PathVariable Long id, @RequestBody AssignmentUpdateRequest dto) {
        Assignment assignment = assignmentService.updateAssignment(id, dto);
        AssignmentResponse response = AssignmentResponse.from(assignment);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "과제 삭제", description = "과제를 삭제합니다.")
    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "과제 제목 검색", description = "제목에 특정 키워드가 포함된 과제를 검색합니다.")
    @GetMapping("/assignments/search")
    public ResponseEntity<List<AssignmentResponse>> searchAssignments(@RequestParam String keyword) {
        List<Assignment> assignments = assignmentService.searchByTitle(keyword);
        List<AssignmentResponse> responses = assignments.stream()
                .map(AssignmentResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}

