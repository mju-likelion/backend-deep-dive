package class5.package1;

import class4.role.Lion;
import class4.role.Role;
import class4.role.Staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberRepository {
    private Map<String, List<Role>> members = new HashMap<>();

    public Map<String, List<Role>> addMember(String name, String role, String part){
        Role newMember;

        if (role.equalsIgnoreCase("lion")){
            newMember = new Lion(name, 1, "Computer", "Backend", 1);
        } else if (role.equalsIgnoreCase("staff")){
            newMember = new Staff(name, 1, "Computer", "Backend", "회장");
        } else {
            System.out.println("wrong input");
            throw new IllegalArgumentException("Invalid role");
        }

        members.computeIfAbsent(part, k -> new ArrayList<>()).add(newMember);

        return members;
    }

    public void getMembers(){
        members.forEach((part, roleList) -> {
            System.out.println("=== " + part + " ===");
            roleList.forEach(m -> System.out.println(m.getDetailInfo()));
        });
    }

    public Role searchMember(String name){
        return members.values().stream()
                .flatMap(List::stream)
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElse(null);
    }


}
