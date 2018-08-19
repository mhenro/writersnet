package com.writersnets.models.entities.contests;

import com.writersnets.models.entities.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "contest_rating")
@Audited
@Getter
@Setter
@NoArgsConstructor
public class ContestRating extends AbstractEntity {
    @EmbeddedId
    private ContestRatingPK pk;
    private Short estimation;
}
