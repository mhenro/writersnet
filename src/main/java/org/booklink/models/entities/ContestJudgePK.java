package org.booklink.models.entities;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by mhenr on 25.02.2018.
 */
@Embeddable
public class ContestJudgePK implements Serializable {
    private Contest contest;
    private User judge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_id")
    public User getJudge() {
        return judge;
    }

    public void setJudge(User judge) {
        this.judge = judge;
    }
}
