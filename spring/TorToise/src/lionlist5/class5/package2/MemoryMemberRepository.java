package class5.package2;

import class5.role.Role;

import java.util.ArrayList;
import java.util.List;

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
        return new ArrayList<>(members);
    }

    @Override
    public boolean existsByName(String name) {
        return findByName(name) != null;
    }
}
