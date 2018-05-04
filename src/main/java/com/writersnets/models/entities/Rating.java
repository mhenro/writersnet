package com.writersnets.models.entities;


import javax.persistence.*;

/**
 * Created by mhenr on 18.10.2017.
 */
@Entity
public class Rating extends AbstractEntity {
    @EmbeddedId
    private RatingId ratingId;


    public RatingId getRatingId() {
        return ratingId;
    }
    public void setRatingId(RatingId ratingId) {
        this.ratingId = ratingId;
    }
    public Integer getEstimation() {
        return ratingId.getEstimation();
    }
}
