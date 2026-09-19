package 패키지1;

import java.util.Scanner;

public class Step2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("🦁 아기사자 이름을 입력해주세요.");
        String name = sc.nextLine();

        System.out.println("🎓 전공을 입력해주세요.");
        String major = sc.nextLine();

        System.out.println("📌 기수를 입력해주세요.");
        String gradeInput = sc.nextLine();

        Lion lion = new Lion(name, major, gradeInput);

        System.out.println("📌 Lion 객체에서 입력값 검증을 진행합니다.");
        if (!lion.isValid()) {
            return;
        }

        System.out.println("⏩ 입력값 검증을 통과하여 아기사자 객체를 사용합니다.");
        System.out.println("✅ 아기사자 객체를 성공적으로 생성하였습니다.");
        System.out.println("🦁 아기사자 정보를 출력합니다.");
        lion.printInfo();

        System.out.println("📌 같은 패키지에서 필드 접근을 확인합니다.");

        lion.name = "패키지1에서 수정한 이름";
        lion.major = "패키지1에서 수정한 전공";

        // lion.grade = 20;
        // private 필드는 같은 패키지에서도 직접 접근할 수 없다.

        System.out.println("✅ 같은 패키지에서는 public, default 필드에 직접 접근할 수 있습니다.");
        lion.printInfo();
    }
}
