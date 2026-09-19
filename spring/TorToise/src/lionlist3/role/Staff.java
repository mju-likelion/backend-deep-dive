package class3.role;

import class3.policy.StaffSubmissionPolicy;
import class3.policy.SubmissionPolicy;

public class Staff extends Member {
    private final String position;

    public Staff(String name, String major, int generation, String part, String position) {
        super(name, major, generation, part);
        this.position = position;
    }

    @Override
    protected SubmissionPolicy getSubmissionPolicy() {
        return new StaffSubmissionPolicy();
    }

    @Override
    public String getDetailInfo() {
        return "[운영진] " + getBaseInfo() + ", 직책: " + position;
    }
}
