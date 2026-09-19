package com.likelion.pbl.week7.repository;

import com.likelion.pbl.week7.domain.role.Role;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MemoryMemberRepository implements MemberRepository {

    private final List<Role> members = new ArrayList<>();

    @Override
    public void save(Role member) {
        members.add(member);
    }

    @Override
    public Role findByName(String name) {
        for (Role member : members) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        return null;
    }

    @Override
    public List<Role> findAll() {
        return members;
    }

    @Override
    public void updateByName(String name, Role member) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getName().equals(name)) {
                members.set(i, member);
                return;
            }
        }
    }

    @Override
    public boolean deleteByName(String name) {
        return members.removeIf(member -> member.getName().equals(name));
    }

    @Override
    public boolean existsByName(String name) {
        for (Role member : members) {
            if (member.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}