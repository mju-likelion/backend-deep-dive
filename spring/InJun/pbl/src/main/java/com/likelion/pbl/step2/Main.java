package com.likelion.pbl.step2;

import com.likelion.pbl.role.Lion;
import com.likelion.pbl.role.Role;
import com.likelion.pbl.role.Staff;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ======= 구현체 선택 =======
        System.out.println("🔧 저장소를 선택하세요:");
        System.out.println("1. MemoryMemberRepository (실제 저장)");
        System.out.println("2. MockMemberRepository (더미 데이터)");
        System.out.print("선택: ");
        int repoChoice = scanner.nextInt();
        scanner.nextLine();
        System.out.println();

        // ======= 의존성 조립 =======
        // 이 한 줄만 바꾸면 Service 코드 수정 없이 동작이 달라진다!
        MemberRepository repository;
        if (repoChoice == 2) {
            repository = new MockMemberRepository();
        } else {
            repository = new MemoryMemberRepository();
        }
        MemberService service = new MemberService(repository);
        // ==========================

        while (true) {
            printMenu();
            int choice = readInt(scanner, "선택: ");

            switch (choice) {
                case 1 -> registerMember(scanner, service);
                case 2 -> showAllMembers(service);
                case 3 -> searchMember(scanner, service);
                case 4 -> {
                    System.out.println("👋 프로그램을 종료합니다.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("❌ 잘못된 선택입니다. 다시 선택해주세요.\n");
            }
        }
    }

    private static void printMenu() {
        System.out.println("🦁 ===== 멋사 멤버 관리 시스템 (Step 2: DI 적용) ===== 🦁");
        System.out.println("1. ➕ 멤버 등록");
        System.out.println("2. 📋 전체 멤버 조회");
        System.out.println("3. 🔍 이름으로 검색");
        System.out.println("4. 🚪 종료");
    }

    private static void registerMember(Scanner scanner, MemberService service) {
        System.out.println("\n👤 역할 선택 (1: 아기사자, 2: 운영진): ");
        int roleChoice = readInt(scanner, "");

        System.out.println("\n📝 정보 입력");
        String name = readString(scanner, "이름: ");
        String major = readString(scanner, "전공: ");
        int generation = readInt(scanner, "기수: ");
        String part = readString(scanner, "파트: ");

        Role member;
        if (roleChoice == 1) {
            String studentId = readString(scanner, "학번: ");
            member = new Lion(name, major, generation, part, studentId);
        } else {
            String position = readString(scanner, "직책: ");
            member = new Staff(name, major, generation, part, position);
        }

        if (service.register(member)) {
            System.out.println("\n✅ 등록 완료: " + member.getName() + "\n");
        } else {
            System.out.println("\n❌ 등록 실패: 이미 존재하는 이름입니다.\n");
        }
    }

    private static void showAllMembers(MemberService service) {
        System.out.println("\n📋 ===== 전체 멤버 목록 =====");

        if (service.isEmpty()) {
            System.out.println("등록된 멤버가 없습니다.\n");
            return;
        }

        List<Role> members = service.getAllMembers();
        for (int i = 0; i < members.size(); i++) {
            Role member = members.get(i);
            System.out.println((i + 1) + ". [" + member.roleName() + "] " + member.getName());
        }
        System.out.println();
    }

    private static void searchMember(Scanner scanner, MemberService service) {
        String name = readString(scanner, "\n🔍 검색할 이름: ");
        Role member = service.searchByName(name);

        if (member == null) {
            System.out.println("❌ 해당 이름의 멤버를 찾을 수 없습니다.\n");
            return;
        }

        System.out.println("\n🎯 ===== 검색 결과 =====");
        System.out.println("👤 역할: " + member.roleName());
        System.out.println(member.getInfo());
        System.out.println("📝 과제 제출 가능: " + (member.canSubmitAssignment() ? "✅ 가능" : "❌ 불가능"));
        System.out.println();
    }

    private static String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}
