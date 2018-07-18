package com.writersnets.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by mhenr on 25.02.2018.
 */
@Embeddable
@Audited
@Getter @Setter @NoArgsConstructor
public class ContestJudgePK implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_id")
    private User judge;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContestJudgePK that = (ContestJudgePK) o;

        if (getContest() != null ? !getContest().equals(that.getContest()) : that.getContest() != null) return false;
        return getJudge() != null ? getJudge().equals(that.getJudge()) : that.getJudge() == null;

    }

    @Override
    public int hashCode() {
        int result = getContest() != null ? getContest().hashCode() : 0;
        result = 31 * result + (getJudge() != null ? getJudge().hashCode() : 0);
        return result;
    }
}
