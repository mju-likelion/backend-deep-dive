package class5.package2;

import class5.role.Lion;
import class5.role.Role;
import class5.role.Staff;

import java.util.ArrayList;
import java.util.List;

public class MockMemberRepository implements MemberRepository {
    private final List<Role> mockMembers = List.of(
            new Lion("모의사자", "컴퓨터공학", 13, "백엔드", "20260001"),
            new Staff("모의운영진", "경영학", 13, "기획", "팀장")
    );

    @Override
    public void save(Role member) {
        System.out.println("[Mock 저장소] 실제 저장은 수행하지 않습니다: " + member.getName());
    }

    @Override
    public Role findByName(String name) {
        for (Role member : mockMembers) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        return null;
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(mockMembers);
    }

    @Override
    public boolean existsByName(String name) {
        return findByName(name) != null;
    }
}
