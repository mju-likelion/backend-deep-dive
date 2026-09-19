package com.likelion.pbl.policy;

public class StaffSubmissionPolicy implements SubmissionPolicy{
    @Override
    public boolean canSubmit() {
        return false;
    }
}
