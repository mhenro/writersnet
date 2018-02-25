package org.booklink.models.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mhenr on 25.02.2018.
 */
@Entity
@Table(name = "contest_participants")
public class ContestParticipant {
    private ContestParticipantPK pk;
    private Boolean accepted;

    @EmbeddedId
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
