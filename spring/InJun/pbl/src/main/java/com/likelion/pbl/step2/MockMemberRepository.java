package com.likelion.pbl.step2;

import com.likelion.pbl.role.Lion;
import com.likelion.pbl.role.Role;
import com.likelion.pbl.role.Staff;

import java.util.ArrayList;
import java.util.List;

public class MockMemberRepository implements MemberRepository{

    private List<Role> dummyMembers;

    public MockMemberRepository() {
        dummyMembers = new ArrayList<>();
        dummyMembers.add(new Lion("김멋사", "컴퓨터공학과", 14, "백엔드", "20210001"));
        dummyMembers.add(new Lion("이사자", "소프트웨어학과", 14, "프론트엔드", "20210002"));
        dummyMembers.add(new Staff("박운영", "정보통신공학과", 12, "백엔드", "대표"));
        System.out.println("🧪 [Mock] 더미 데이터 " + dummyMembers.size() + "개가 준비되었습니다.");
    }

    @Override
    public void save(Role member) {
        System.out.println("🧪 [Mock] 저장 요청됨 (실제 저장 안 함): " + member.getName());
    }

    @Override
    public Role findByName(String name) {
        for (Role member : dummyMembers) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        return null;
    }

    @Override
    public List<Role> findAll() {
        return dummyMembers;
    }

    @Override
    public boolean existsByName(String name) {
        for (Role member : dummyMembers) {
            if (member.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
