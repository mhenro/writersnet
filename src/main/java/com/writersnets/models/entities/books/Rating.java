package com.writersnets.models.entities.books;


import com.writersnets.models.entities.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Created by mhenr on 18.10.2017.
 */
@Entity
@Table(name = "ratings")
@Audited
@Getter @Setter @NoArgsConstructor
public class Rating extends AbstractEntity {
    @EmbeddedId
    private RatingId ratingId;

    public Integer getEstimation() {
        return ratingId.getEstimation();
    }
}
