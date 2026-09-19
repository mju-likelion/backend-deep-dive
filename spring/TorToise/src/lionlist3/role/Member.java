package class3.role;

import class3.policy.SubmissionPolicy;

public abstract class Member {
    private final String name;
    private final String major;
    private final int generation;
    private final String part;

    protected Member(String name, String major, int generation, String part) {
        this.name = name;
        this.major = major;
        this.generation = generation;
        this.part = part;
    }

    public boolean canSubmitAssignment() {
        return getSubmissionPolicy().canSubmit();
    }

    protected abstract SubmissionPolicy getSubmissionPolicy();

    public abstract String getDetailInfo();

    protected String getBaseInfo() {
        return "이름: " + name
                + ", 전공: " + major
                + ", 기수: " + generation
                + ", 파트: " + part;
    }
}
