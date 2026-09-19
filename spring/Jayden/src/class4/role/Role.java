package class4.role;

import class4.policy.SubmissionPolicy;

public abstract class Role {
    private String name;
    private int cd;
    private String major;
    private String part;

    public Role(String name, int cd, String major, String part){
        this.name = name;
        this.cd = cd;
        this.major = major;
        this.part = part;
    }

    public String getName() {
        return name;
    }

    public int getCd() {
        return cd;
    }

    public String getMajor() {
        return major;
    }

    public String getPart() {
        return part;
    }

    public abstract SubmissionPolicy getPolicy();

    public abstract String getDetailInfo();

    public boolean submit(){
        return getPolicy().checkSub();
    }
}
