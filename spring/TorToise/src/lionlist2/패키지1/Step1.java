package 패키지1;

import java.util.Scanner;

public class Step1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("🦁 아기사자 이름을 입력해주세요.");
        String name = sc.nextLine();

        System.out.println("🎓 전공을 입력해주세요.");
        String major = sc.nextLine();

        System.out.println("📌 기수를 입력해주세요.");
        String gradeInput = sc.nextLine();

        boolean isValid = true;
        int grade = -1;

        System.out.println("📌 입력값 검증을 진행합니다.");

        if (name == null || name.isBlank()) {
            System.out.println("❌ 이름은 비어 있을 수 없습니다.");
            isValid = false;
        }

        if (major == null || major.isBlank()) {
            System.out.println("❌ 전공은 비어 있을 수 없습니다.");
            isValid = false;
        }

        try {
            grade = Integer.parseInt(gradeInput.trim());
            if (grade < 1) {
                System.out.println("❌ 기수는 1 이상이어야 합니다.");
                isValid = false;
            }
        } catch (Exception e) {
            System.out.println("❌ 기수는 숫자로 입력해야 합니다.");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        System.out.println("⏩ 입력값 검증을 통과하여 아기사자 객체 생성을 진행합니다.");
        Lion lion = new Lion(name, major, grade);
        System.out.println("✅ 아기사자 객체를 성공적으로 생성하였습니다.");
        System.out.println("🦁 아기사자 정보를 출력합니다.");
        lion.printInfo();
    }
}
