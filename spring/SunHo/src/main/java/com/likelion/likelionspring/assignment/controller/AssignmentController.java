package com.likelion.likelionspring.assignment.controller;

import com.likelion.likelionspring.assignment.dto.AssignmentCreateRequest;
import com.likelion.likelionspring.assignment.dto.AssignmentResponse;
import com.likelion.likelionspring.assignment.service.AssignmentService;
import com.likelion.likelionspring.global.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/assignments")
    public ResponseEntity<PageResponse<AssignmentResponse>> findAll(Pageable pageable) {
        Page<AssignmentResponse> responses = assignmentService.findAll(pageable)
                .map(AssignmentResponse::new);
        return ResponseEntity.ok(new PageResponse<>(responses));
    }

    @GetMapping("/assignments/search")
    public ResponseEntity<List<AssignmentResponse>> search(@RequestParam String keyword) {
        List<AssignmentResponse> responses = assignmentService.searchByKeyword(keyword).stream()
                .map(AssignmentResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/assignments")
    public ResponseEntity<AssignmentResponse> save(
            @AuthenticationPrincipal Long memberId,
            @RequestBody AssignmentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AssignmentResponse(assignmentService.save(memberId, request.getTitle(), request.getDescription())));
    }

    @GetMapping("/members/{memberId}/assignments")
    public ResponseEntity<List<AssignmentResponse>> findByMemberId(@PathVariable Long memberId) {
        List<AssignmentResponse> responses = assignmentService.findByMemberId(memberId)
                .stream()
                .map(AssignmentResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/assignments/{id}")
    public ResponseEntity<AssignmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new AssignmentResponse(assignmentService.findById(id)));
    }

    @PutMapping("/assignments/{id}")
    public ResponseEntity<AssignmentResponse> update(
            @PathVariable Long id,
            @AuthenticationPrincipal Long memberId,
            @RequestBody AssignmentCreateRequest request) {
        return ResponseEntity.ok(new AssignmentResponse(assignmentService.update(id, memberId, request.getTitle(), request.getDescription())));
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Long memberId) {
        assignmentService.delete(id, memberId);
        return ResponseEntity.noContent().build();
    }
}