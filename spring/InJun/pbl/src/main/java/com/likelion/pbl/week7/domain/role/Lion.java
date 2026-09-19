package com.likelion.pbl.week7.domain.role;

public class Lion extends Role {

    private String studentId;

    public Lion(String name, String major, int generation, String part, String studentId) {
        super(name, major, generation, part);
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    @Override
    public String roleName() {
        return "아기사자";
    }

    @Override
    public String getInfo() {
        return "📌 이름: " + getName()
                + " | 🎓 전공: " + getMajor()
                + " | 🔢 기수: " + getGeneration()
                + " | 💻 파트: " + getPart()
                + "\n🆔 학번: " + studentId;
    }
}