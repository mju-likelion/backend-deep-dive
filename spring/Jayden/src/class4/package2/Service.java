package class4.package2;

import class4.role.Lion;
import class4.role.Role;
import class4.role.Staff;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Service {
    public Map<String, List<Role>> addMember(Map<String, List<Role>> members, String name, String role, String part){
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

    public void getMembers(Map<String, List<Role>> members){
        members.forEach((part, roleList) -> {
            System.out.println("=== " + part + " ===");
            roleList.forEach(m -> System.out.println(m.getDetailInfo()));
        });
    }

    public Role searchMember(Map<String, List<Role>> members, String name){
        return members.values().stream()
                .flatMap(List::stream)
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void getMembersByPart(Map<String, List<Role>> members, String part){
        List<Role> roleList = members.getOrDefault(part, new ArrayList<>());
        if (roleList.isEmpty()) {
            System.out.println("No members in " + part);
        } else {
            roleList.forEach(m -> System.out.println(m.getDetailInfo()));
        }
    }
}
