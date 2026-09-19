package com.BabyLion.Spring.member.domain;

import com.BabyLion.Spring.assignment.domain.Assignment;
import com.BabyLion.Spring.comment.domain.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 내부에서만 쓰도록 protected
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    String major;
    String part;
    int generation;
    @Enumerated(EnumType.STRING)
    RoleType roleType;
    String studentId;
    String position;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Assignment> assignments = new ArrayList<>();
    String password;
    @Column(unique = true)
    String loginId;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    //id는 자동 생성하므로 생성자에서 제외(GenerateValue)
    public Member(String name, String major, String part, int generation, RoleType roleType, String studentId, String position, String password, String loginId) {
        this.name = name;
        this.major = major;
        this.part = part;
        this.generation = generation;
        this.roleType = roleType;
        this.studentId = studentId;
        this.position = position;
        this.password = password;
        this.loginId = loginId;
    }

//    protected Member() {}

    public void updateInfo(String name, String major, String part, int generation){
        this.name = name;
        this.major = major;
        this.part = part;
        this.generation = generation;
    }

    public void updateStudentID(String studentId){
        this.studentId = studentId;
    }

    public void updatePosition(String position){
        this.position = position;
    }
}
