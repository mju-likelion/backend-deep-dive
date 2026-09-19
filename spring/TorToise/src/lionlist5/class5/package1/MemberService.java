package class5.package1;

import class5.role.Role;

import java.util.List;

public class MemberService {
    private final MemberRepository memberRepository = new MemberRepository();

    public boolean register(Role member) {
        if (memberRepository.existsByName(member.getName())) {
            return false;
        }

        memberRepository.save(member);
        return true;
    }

    public Role findByName(String name) {
        return memberRepository.findByName(name);
    }

    public List<Role> findAll() {
        return memberRepository.findAll();
    }
}
