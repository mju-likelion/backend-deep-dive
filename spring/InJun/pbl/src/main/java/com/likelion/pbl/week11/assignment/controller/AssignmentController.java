package com.likelion.pbl.week11.assignment.controller;

import com.likelion.pbl.week11.assignment.domain.Assignment;
import com.likelion.pbl.week11.assignment.dto.AssignmentCreateRequest;
import com.likelion.pbl.week11.assignment.dto.AssignmentCreateWithMemberRequest;
import com.likelion.pbl.week11.assignment.dto.AssignmentResponse;
import com.likelion.pbl.week11.assignment.dto.AssignmentUpdateRequest;
import com.likelion.pbl.week11.assignment.service.AssignmentService;
import com.likelion.pbl.week11.global.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/members/{memberId}/assignments")
    public ResponseEntity<AssignmentResponse> createAssignment(
            @PathVariable Long memberId,
            @Valid @RequestBody AssignmentCreateRequest request
    ) {
        Assignment assignment = assignmentService.createAssignment(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(AssignmentResponse.from(assignment));
    }

    @PostMapping("/assignments")
    public ResponseEntity<AssignmentResponse> createAssignment(
            @Valid @RequestBody AssignmentCreateWithMemberRequest request
    ) {
        Assignment assignment = assignmentService.createAssignment(
                request.getMemberId(),
                request.toAssignmentCreateRequest()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(AssignmentResponse.from(assignment));
    }

    @GetMapping("/assignments")
    public ResponseEntity<PageResponse<AssignmentResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AssignmentResponse> responses = assignmentService.findAll(pageable)
                .map(AssignmentResponse::from);

        return ResponseEntity.ok(PageResponse.from(responses));
    }

    @GetMapping("/assignments/search")
    public ResponseEntity<PageResponse<AssignmentResponse>> searchByTitle(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AssignmentResponse> responses = assignmentService.searchByTitle(keyword, pageable)
                .map(AssignmentResponse::from);

        return ResponseEntity.ok(PageResponse.from(responses));
    }

    @GetMapping("/members/{memberId}/assignments")
    public ResponseEntity<PageResponse<AssignmentResponse>> findByMemberId(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AssignmentResponse> responses = assignmentService.findByMemberId(memberId, pageable)
                .map(AssignmentResponse::from);

        return ResponseEntity.ok(PageResponse.from(responses));
    }

    @GetMapping("/assignments/{id}")
    public ResponseEntity<AssignmentResponse> findById(@PathVariable Long id) {
        Assignment assignment = assignmentService.findById(id);
        return ResponseEntity.ok(AssignmentResponse.from(assignment));
    }

    @PutMapping("/assignments/{id}")
    public ResponseEntity<AssignmentResponse> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody AssignmentUpdateRequest request
    ) {
        Assignment assignment = assignmentService.updateAssignment(id, request);
        return ResponseEntity.ok(AssignmentResponse.from(assignment));
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
