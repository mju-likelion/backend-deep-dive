package com.likelion.pbl.step2;

import com.likelion.pbl.role.Role;

import java.util.List;

public interface MemberRepository {

    void save(Role member);
    Role findByName(String name);
    List<Role> findAll();
    boolean existsByName(String name);
}
