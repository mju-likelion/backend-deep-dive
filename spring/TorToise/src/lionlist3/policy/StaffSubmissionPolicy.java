package class3.policy;

public class StaffSubmissionPolicy implements SubmissionPolicy {
    @Override
    public boolean canSubmit() {
        return false;
    }
}
