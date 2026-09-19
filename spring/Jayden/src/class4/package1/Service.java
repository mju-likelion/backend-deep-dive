package class4.package1;

import class4.role.Lion;
import class4.role.Role;
import class4.role.Staff;

import java.util.List;

public class Service {
    public List<Role> addMember(List<Role> members, String name, String role){
        Role newMember;

        if (role.equalsIgnoreCase("lion")){
            newMember = new Lion(name, 1, "Computer", "Backend", 1);
        } else if (role.equalsIgnoreCase("staff")){
            newMember = new Staff(name, 1, "Computer", "Backend", "회장");
        } else {
            System.out.println("wrong input");
            throw new IllegalArgumentException("Invalid role");
        }

        if (members.stream().anyMatch(m -> m.getName().equals(name))){
            System.out.println(name + " already exists");
        } else {
            members.add(newMember);
            System.out.println(newMember.getDetailInfo());
            System.out.println("Successfully added.");
        }

        return members;
    }

    public void getMembers(List<Role> members){
        members.forEach(m -> System.out.println(m.getDetailInfo()));
    }

    public Role searchMember(List<Role> members, String name){
        return members.stream()
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
