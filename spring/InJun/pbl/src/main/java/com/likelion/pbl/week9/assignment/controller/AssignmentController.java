package com.likelion.pbl.week9.assignment.controller;

import com.likelion.pbl.week9.assignment.domain.Assignment;
import com.likelion.pbl.week9.assignment.dto.AssignmentCreateRequest;
import com.likelion.pbl.week9.assignment.dto.AssignmentResponse;
import com.likelion.pbl.week9.assignment.dto.AssignmentUpdateRequest;
import com.likelion.pbl.week9.assignment.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/members/{memberId}/assignments")
    public ResponseEntity<?> createAssignment(
            @PathVariable Long memberId,
            @RequestBody AssignmentCreateRequest request
    ) {
        Assignment assignment = assignmentService.createAssignment(memberId, request);

        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(201).body(AssignmentResponse.from(assignment));
    }

    @GetMapping("/members/{memberId}/assignments")
    public ResponseEntity<List<AssignmentResponse>> findByMemberId(@PathVariable Long memberId) {
        List<AssignmentResponse> responses = assignmentService.findByMemberId(memberId)
                .stream()
                .map(AssignmentResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/assignments/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Assignment assignment = assignmentService.findById(id);

        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(AssignmentResponse.from(assignment));
    }

    @PutMapping("/assignments/{id}")
    public ResponseEntity<?> updateAssignment(
            @PathVariable Long id,
            @RequestBody AssignmentUpdateRequest request
    ) {
        Assignment assignment = assignmentService.updateAssignment(id, request);

        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(AssignmentResponse.from(assignment));
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        boolean deleted = assignmentService.deleteAssignment(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assignments")
    public ResponseEntity<List<AssignmentResponse>> searchAssignments(
            @RequestParam String title
    ) {
        List<AssignmentResponse> responses = assignmentService.searchByTitle(title)
                .stream()
                .map(AssignmentResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}
