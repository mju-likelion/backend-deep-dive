package class5.package1;

import class5.role.Role;

import java.util.ArrayList;
import java.util.List;

public class MemberRepository {
    private final List<Role> members = new ArrayList<>();

    public void save(Role member) {
        members.add(member);
    }

    public Role findByName(String name) {
        for (Role member : members) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        return null;
    }

    public List<Role> findAll() {
        return new ArrayList<>(members);
    }

    public boolean existsByName(String name) {
        return findByName(name) != null;
    }
}
