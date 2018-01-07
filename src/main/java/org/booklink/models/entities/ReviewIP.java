package org.booklink.models.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mhenr on 07.01.2018.
 */
@Entity
@Table(name = "reviews_ip")
public class ReviewIP {
    private ReviewIP_PK reviewPK;

    @EmbeddedId
    public ReviewIP_PK getReviewPK() {
        return reviewPK;
    }

    public void setReviewPK(ReviewIP_PK reviewPK) {
        this.reviewPK = reviewPK;
    }
}
