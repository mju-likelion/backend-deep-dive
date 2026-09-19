package class3.role;

import class3.policy.LionSubmissionPolicy;
import class3.policy.SubmissionPolicy;

public class Lion extends Member {
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
