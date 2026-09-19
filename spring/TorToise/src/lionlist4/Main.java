import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MemberManager manager = new MemberManager();
        manager.run();
    }
}

abstract class Role {
    private final String name;
    private final int age;
    private final String part;

    protected Role(String name, int age, String part) {
        this.name = name;
        this.age = age;
        this.part = part;
    }

    public String getName() {
        return name;
    }

    public String getPart() {
        return part;
    }

    public abstract String getRoleName();

    public abstract String getActivity();

    public void printInfo() {
        System.out.printf(
                "[%s] 이름: %s, 나이: %d, 파트: %s, 활동: %s%n",
                getRoleName(),
                name,
                age,
                part,
                getActivity()
        );
    }
}

class Lion extends Role {
    private final int generation;

    public Lion(String name, int age, String part, int generation) {
        super(name, age, part);
        this.generation = generation;
    }

    @Override
    public String getRoleName() {
        return "아기사자";
    }

    @Override
    public String getActivity() {
        return generation + "기 아기사자로 과제를 수행합니다.";
    }
}

class Staff extends Role {
    private final String position;

    public Staff(String name, int age, String part, String position) {
        super(name, age, part);
        this.position = position;
    }

    @Override
    public String getRoleName() {
        return "운영진";
    }

    @Override
    public String getActivity() {
        return position + " 역할로 멤버를 지원합니다.";
    }
}

class MemberManager {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Role> members = new ArrayList<>();
    private final Map<String, List<Role>> membersByPart = new LinkedHashMap<>();

    public void run() {
        while (true) {
            printMenu();
            int menu = inputInt("메뉴 선택: ");

            switch (menu) {
                case 1 -> registerMember();
                case 2 -> printAllMembers();
                case 3 -> searchMemberByName();
                case 4 -> printMembersByPart();
                case 0 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 메뉴입니다.");
            }

            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("==== 멋쟁이사자처럼 멤버 관리 시스템 ====");
        System.out.println("1. 멤버 등록");
        System.out.println("2. 전체 멤버 조회");
        System.out.println("3. 이름으로 멤버 검색");
        System.out.println("4. 파트별 멤버 조회");
        System.out.println("0. 종료");
    }

    private void registerMember() {
        System.out.println("1. 아기사자");
        System.out.println("2. 운영진");
        int roleType = inputInt("역할 선택: ");

        if (roleType != 1 && roleType != 2) {
            System.out.println("등록 실패: 존재하지 않는 역할입니다.");
            return;
        }

        String name = inputString("이름: ");
        if (findByName(name) != null) {
            System.out.println("등록 실패: 이미 같은 이름의 멤버가 있습니다.");
            return;
        }

        int age = inputInt("나이: ");
        String part = inputString("파트: ");
        Role member;

        if (roleType == 1) {
            int generation = inputInt("기수: ");
            member = new Lion(name, age, part, generation);
        } else {
            String position = inputString("운영진 직책: ");
            member = new Staff(name, age, part, position);
        }

        members.add(member);
        membersByPart.computeIfAbsent(part, key -> new ArrayList<>()).add(member);
        System.out.println("등록 완료: " + name + " 멤버가 등록되었습니다.");
    }

    private void printAllMembers() {
        if (members.isEmpty()) {
            System.out.println("등록된 멤버가 없습니다.");
            return;
        }

        System.out.println("==== 전체 멤버 목록 ====");
        for (Role member : members) {
            member.printInfo();
        }
    }

    private void searchMemberByName() {
        String name = inputString("검색할 이름: ");
        Role member = findByName(name);

        if (member == null) {
            System.out.println("검색 결과가 없습니다.");
            return;
        }

        System.out.println("==== 검색 결과 ====");
        member.printInfo();
    }

    private void printMembersByPart() {
        if (membersByPart.isEmpty()) {
            System.out.println("등록된 파트가 없습니다.");
            return;
        }

        System.out.println("등록된 파트: " + String.join(", ", membersByPart.keySet()));
        String part = inputString("조회할 파트: ");
        List<Role> partMembers = membersByPart.get(part);

        if (partMembers == null || partMembers.isEmpty()) {
            System.out.println("해당 파트에 등록된 멤버가 없습니다.");
            return;
        }

        System.out.println("==== " + part + " 파트 멤버 목록 ====");
        for (Role member : partMembers) {
            member.printInfo();
        }
    }

    private Role findByName(String name) {
        for (Role member : members) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        return null;
    }

    private String inputString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("빈 값은 입력할 수 없습니다.");
        }
    }

    private int inputInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }
}
