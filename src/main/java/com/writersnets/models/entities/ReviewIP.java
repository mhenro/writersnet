package com.writersnets.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mhenr on 07.01.2018.
 */
@Entity
@Table(name = "reviews_ip")
@Audited
@Getter @Setter @NoArgsConstructor
public class ReviewIP extends AbstractEntity {
    @EmbeddedId
    private ReviewIP_PK reviewPK;
}
