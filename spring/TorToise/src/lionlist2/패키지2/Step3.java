package 패키지2;

import 패키지1.Lion;

public class Step3 {
    public static void main(String[] args) {
        Lion lion = new Lion("홍길동", "컴퓨터공학", 13);

        // public 필드는 다른 패키지에서도 직접 접근할 수 있다.
        lion.name = "패키지2에서 변경한 이름";

        // default 필드는 다른 패키지에서 직접 접근할 수 없다.
        // lion.major = "수학";

        // private 필드는 선언된 클래스 외부에서 직접 접근할 수 없다.
        // lion.grade = 20;

        lion.printInfo();
        System.out.println("getter로 조회한 기수: " + lion.getGrade());
    }
}
