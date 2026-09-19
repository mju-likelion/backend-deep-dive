package class5.package2;

import class5.role.Role;

import java.util.List;

public interface MemberRepository {
    void save(Role member);

    Role findByName(String name);

    List<Role> findAll();

    boolean existsByName(String name);
}
