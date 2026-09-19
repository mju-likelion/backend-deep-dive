package class4.package1;

import class4.role.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Service service = new Service();
        List<Role> members = new ArrayList<>();

        while (true) {
            System.out.println("\n1. Add Member | 2. Get All Members | 3. Search Member | 4. Exit");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Role (lion/staff): ");
                    String role = scanner.nextLine();

                    try {
                        service.addMember(members, name, role);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "2" -> service.getMembers(members);
                case "3" -> {
                    System.out.print("Search name: ");
                    String name = scanner.nextLine();

                    Role found = service.searchMember(members, name);
                    if (found != null) {
                        System.out.println(found.getDetailInfo());
                    } else {
                        System.out.println("Member not found.");
                    }
                }
                case "4" -> {
                    System.out.println("Goodbye");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }
}
