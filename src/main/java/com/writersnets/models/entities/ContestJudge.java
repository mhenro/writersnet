package com.writersnets.models.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mhenr on 25.02.2018.
 */
@Entity
@Table(name = "contest_judges")
public class ContestJudge extends AbstractEntity {
    @EmbeddedId
    private ContestJudgePK pk;
    private Boolean accepted;

    public ContestJudgePK getPk() {
        return pk;
    }
    public void setPk(ContestJudgePK pk) {
        this.pk = pk;
    }
    public Boolean getAccepted() {
        return accepted;
    }
    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
