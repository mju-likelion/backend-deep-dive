package class5.role;

import class5.policy.SubmissionPolicy;

public abstract class Role {
    private final String name;
    private final String major;
    private final int generation;
    private final String part;

    protected Role(String name, String major, int generation, String part) {
        this.name = name;
        this.major = major;
        this.generation = generation;
        this.part = part;
    }

    public String getName() {
        return name;
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
