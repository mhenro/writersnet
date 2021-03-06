package com.writersnets.models.entities.contests;

import com.writersnets.models.entities.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mhenr on 25.02.2018.
 */
@Entity
@Table(name = "contest_participants")
@Audited
@Getter @Setter @NoArgsConstructor
public class ContestParticipant extends AbstractEntity {
    @EmbeddedId
    private ContestParticipantPK pk;
    private Boolean accepted;
}
