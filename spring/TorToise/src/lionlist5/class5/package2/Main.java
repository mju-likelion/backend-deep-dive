package class5.package2;

import class5.role.Lion;
import class5.role.Role;
import class5.role.Staff;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        MemberRepository memberRepository = selectRepository();
        MemberService memberService = new MemberService(memberRepository);

        while (true) {
            printMenu();
            String menu = scanner.nextLine();

            if (menu.equals("1")) {
                registerMember(memberService);
            } else if (menu.equals("2")) {
                printAllMembers(memberService);
            } else if (menu.equals("3")) {
                searchMember(memberService);
            } else if (menu.equals("0")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else {
                System.out.println("올바른 메뉴를 선택해주세요.");
            }
        }
    }

    private static MemberRepository selectRepository() {
        System.out.print("저장소 선택(1. Memory, 2. Mock): ");
        String repositoryType = scanner.nextLine();

        if (repositoryType.equals("2")) {
            System.out.println("MockMemberRepository를 주입합니다.");
            return new MockMemberRepository();
        }

        System.out.println("MemoryMemberRepository를 주입합니다.");
        return new MemoryMemberRepository();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== Step 2 멤버 관리 ===");
        System.out.println("1. 멤버 등록");
        System.out.println("2. 전체 멤버 조회");
        System.out.println("3. 이름으로 검색");
        System.out.println("0. 종료");
        System.out.print("선택: ");
    }

    private static void registerMember(MemberService memberService) {
        Role member = createMember();
        boolean registered = memberService.register(member);

        if (registered) {
            System.out.println("등록 완료: " + member.getName());
        } else {
            System.out.println("등록 실패: 이미 존재하는 이름입니다.");
        }
    }

    private static Role createMember() {
        System.out.print("역할 선택(1. 아기사자, 2. 운영진): ");
        String role = scanner.nextLine();
        String name = input("이름");
        String major = input("전공");
        int generation = inputGeneration();
        String part = input("파트");

        if (role.equals("2")) {
            String position = input("직책");
            return new Staff(name, major, generation, part, position);
        }

        String studentNumber = input("학번");
        return new Lion(name, major, generation, part, studentNumber);
    }

    private static void printAllMembers(MemberService memberService) {
        List<Role> members = memberService.findAll();
        if (members.isEmpty()) {
            System.out.println("등록된 멤버가 없습니다.");
            return;
        }

        for (Role member : members) {
            printMember(member);
        }
    }

    private static void searchMember(MemberService memberService) {
        String name = input("검색할 이름");
        Role member = memberService.findByName(name);

        if (member == null) {
            System.out.println("해당 이름의 멤버를 찾을 수 없습니다.");
            return;
        }

        printMember(member);
    }

    private static void printMember(Role member) {
        System.out.println(member.getDetailInfo());
        System.out.println("과제 제출 가능 여부: " + member.canSubmitAssignment());
    }

    private static String input(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine();
    }

    private static int inputGeneration() {
        while (true) {
            try {
                return Integer.parseInt(input("기수"));
            } catch (NumberFormatException e) {
                System.out.println("기수는 숫자로 입력해주세요.");
            }
        }
    }
}
