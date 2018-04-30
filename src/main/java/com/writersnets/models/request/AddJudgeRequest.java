package com.writersnets.models.request;

/**
 * Created by mhenr on 09.03.2018.
 */
public class AddJudgeRequest {
    private String judges;
    private Long contestId;

    public String getJudges() {
        return judges;
    }

    public void setJudges(String judges) {
        this.judges = judges;
    }

    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }
}
