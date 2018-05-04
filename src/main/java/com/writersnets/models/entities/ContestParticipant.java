package com.writersnets.models.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mhenr on 25.02.2018.
 */
@Entity
@Table(name = "contest_participants")
public class ContestParticipant extends AbstractEntity {
    @EmbeddedId
    private ContestParticipantPK pk;
    private Boolean accepted;

    public ContestParticipantPK getPk() {
        return pk;
    }
    public void setPk(ContestParticipantPK pk) {
        this.pk = pk;
    }
    public Boolean getAccepted() {
        return accepted;
    }
    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
