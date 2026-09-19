package class5.role;

import class5.policy.LionSubmissionPolicy;
import class5.policy.SubmissionPolicy;

public class Lion extends Role {
    private final String studentNumber;

    public Lion(String name, String major, int generation, String part, String studentNumber) {
        super(name, major, generation, part);
        this.studentNumber = studentNumber;
    }

    @Override
    protected SubmissionPolicy getSubmissionPolicy() {
        return new LionSubmissionPolicy();
    }

    @Override
    public String getDetailInfo() {
        return "[아기사자] " + getBaseInfo() + ", 학번: " + studentNumber;
    }
}
