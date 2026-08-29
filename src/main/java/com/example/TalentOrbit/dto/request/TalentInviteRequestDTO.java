package com.example.TalentOrbit.dto.request;

public class TalentInviteRequestDTO {
    private Long recruiterUserId;
    private Long studentUserId;
    private Long postingId;

    public TalentInviteRequestDTO() {}

    public Long getRecruiterUserId() { return recruiterUserId; }
    public void setRecruiterUserId(Long recruiterUserId) { this.recruiterUserId = recruiterUserId; }

    public Long getStudentUserId() { return studentUserId; }
    public void setStudentUserId(Long studentUserId) { this.studentUserId = studentUserId; }

    public Long getPostingId() { return postingId; }
    public void setPostingId(Long postingId) { this.postingId = postingId; }
}
