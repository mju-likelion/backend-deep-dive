package com.likelion.pbl.week11.domain;

import com.likelion.pbl.week11.assignment.domain.Assignment;
import com.likelion.pbl.week11.comment.domain.Comment;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String major;
    private int generation;
    private String part;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String studentId;
    private String position;
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    protected Member() {
    }

    public Member(String name, String major, int generation, String part,
                  RoleType roleType, String studentId, String position) {
        this.name = name;
        this.major = major;
        this.generation = generation;
        this.part = part;
        this.roleType = roleType;
        this.studentId = studentId;
        this.position = position;
    }

    public Member(String name, String major, int generation, String part,
                  RoleType roleType, String studentId, String position, String password) {
        this(name, major, generation, part, roleType, studentId, position);
        this.password = password;
    }

    public void updateInfo(String major, int generation, String part) {
        this.major = major;
        this.generation = generation;
        this.part = part;
    }

    public void updateStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void updatePosition(String position) {
        this.position = position;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
        assignment.assignMember(this);
    }

    public void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
        assignment.assignMember(null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMajor() {
        return major;
    }

    public int getGeneration() {
        return generation;
    }

    public String getPart() {
        return part;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getPosition() {
        return position;
    }

    public String getPassword() {
        return password;
    }

    public List<Assignment> getAssignments() {
        return Collections.unmodifiableList(assignments);
    }
}
