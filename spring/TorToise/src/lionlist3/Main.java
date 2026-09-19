package class3;

import class3.role.Lion;
import class3.role.Member;
import class3.role.Staff;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("아기사자 정보를 입력합니다.");
        Member lion = createLion(scanner);

        System.out.println();
        System.out.println("운영진 정보를 입력합니다.");
        Member staff = createStaff(scanner);

        System.out.println();
        printMember(lion);
        printMember(staff);
    }

    private static Member createLion(Scanner scanner) {
        String name = input(scanner, "이름");
        String major = input(scanner, "전공");
        int generation = inputGeneration(scanner);
        String part = input(scanner, "파트");
        String studentNumber = input(scanner, "학번");

        return new Lion(name, major, generation, part, studentNumber);
    }

    private static Member createStaff(Scanner scanner) {
        String name = input(scanner, "이름");
        String major = input(scanner, "전공");
        int generation = inputGeneration(scanner);
        String part = input(scanner, "파트");
        String position = input(scanner, "직책");

        return new Staff(name, major, generation, part, position);
    }

    private static String input(Scanner scanner, String label) {
        System.out.print(label + ": ");
        return scanner.nextLine();
    }

    private static int inputGeneration(Scanner scanner) {
        while (true) {
            System.out.print("기수: ");

            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("기수는 숫자로 입력해주세요.");
            }
        }
    }

    private static void printMember(Member member) {
        System.out.println(member.getDetailInfo());
        System.out.println("과제 제출 가능 여부: " + member.canSubmitAssignment());
        System.out.println();
    }
}
