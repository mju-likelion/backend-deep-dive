package com.likelion.pbl.week11.repository;

import com.likelion.pbl.week11.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);

    List<Member> findByPart(String part);

    Page<Member> findByPart(String part, Pageable pageable);

    boolean existsByName(String name);
}
