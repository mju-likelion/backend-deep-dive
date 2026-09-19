package class4.package2;

import class4.role.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Service service = new Service();
        Map<String, List<Role>> members = new HashMap<>();

        while (true) {
            System.out.println("\n1. Add Member | 2. Get All Members | 3. Search Member | 4. Search By Part | 5. Exit");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Role (lion/staff): ");
                    String role = scanner.nextLine();

                    System.out.print("Part (Backend/Frontend etc): ");
                    String part = scanner.nextLine();

                    try {
                        service.addMember(members, name, role, part);
                        System.out.println("Successfully added");
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
                    System.out.print("Part (Backend/Frontend etc): ");
                    String part = scanner.nextLine();
                    service.getMembersByPart(members, part);
                }
                case "5" -> {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }
}