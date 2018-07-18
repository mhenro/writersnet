package com.writersnets.models.entities;

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
@Table(name = "contest_judges")
@Audited
@Getter @Setter @NoArgsConstructor
public class ContestJudge extends AbstractEntity {
    @EmbeddedId
    private ContestJudgePK pk;
    private Boolean accepted;
}
