package 패키지1;

public class Lion {
    public String name;
    String major;
    private int grade;

    public Lion(String name, String major, int grade) {
        this.name = name;
        this.major = major;
        this.grade = grade;
    }

    public Lion(String name, String major, String gradeInput) {
        this.name = name;
        this.major = major;
        this.grade = parseGrade(gradeInput);
    }

    private int parseGrade(String gradeInput) {
        try {
            return Integer.parseInt(gradeInput.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean isValid() {
        if (name == null || name.isBlank()) {
            System.out.println("❌ 이름은 비어 있을 수 없습니다.");
            return false;
        }

        if (major == null || major.isBlank()) {
            System.out.println("❌ 전공은 비어 있을 수 없습니다.");
            return false;
        }

        if (grade < 1) {
            System.out.println("❌ 기수는 1 이상이어야 합니다.");
            return false;
        }

        return true;
    }

    public void printInfo() {
        System.out.println("👤 이름: " + name + " |  🎓 전공: " + major + " |  📌 기수: " + grade);
    }

    public int getGrade() {
        return grade;
    }
}
