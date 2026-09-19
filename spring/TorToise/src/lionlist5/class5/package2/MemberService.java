package class5.package2;

import class5.role.Role;

import java.util.List;

public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

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
