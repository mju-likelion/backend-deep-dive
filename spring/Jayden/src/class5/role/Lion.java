package class5.role;

import class4.policy.LionSubmissionPolicy;
import class4.policy.SubmissionPolicy;

public class Lion extends Role {
    private final int id;
    private final SubmissionPolicy policy = new LionSubmissionPolicy();

    public Lion(String name, int cd, String major, String part, int id) {
        super(name, cd, major, part);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public SubmissionPolicy getPolicy(){
        return policy;
    }

    @Override
    public String getDetailInfo(){
        return "🦁 역할: 아기사자\n"
                + "👤 이름: " + getName()
                + " | 🎓 전공: " + getMajor()
                + " | 📌 기수: " + getCd()
                + " | 💻 파트: " + getPart() + "\n"
                + "🆔 학번: " + id;
    }
}
