package class5.role;

import class4.policy.StaffSubmissionPolicy;
import class4.policy.SubmissionPolicy;

public class Staff extends Role {
    private final String position;
    private final SubmissionPolicy policy = new StaffSubmissionPolicy();

    public Staff(String name, int cd, String major, String part, String position) {
        super(name, cd, major, part);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public SubmissionPolicy getPolicy(){
        return policy;
    }

    @Override
    public String getDetailInfo(){
        return "🦸 역할: 운영진\n"
                + "👤 이름: " + getName()
                + " | 🎓 전공: " + getMajor()
                + " | 📌 기수: " + getCd()
                + " | 💻 파트: " + getPart() + "\n"
                + "⭐ 직책: " + position;
    }
}
