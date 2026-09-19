package com.likelion.pbl.week9.repository;

import com.likelion.pbl.week9.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);

    boolean existsByName(String name);
}