package com.writersnets.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by mhenr on 18.10.2017.
 */
@Entity
public class Rating {
    private RatingId ratingId;

    @EmbeddedId
    public RatingId getRatingId() {
        return ratingId;
    }

    public void setRatingId(RatingId ratingId) {
        this.ratingId = ratingId;
    }

    @Transient
    @JsonIgnore
    public Integer getEstimation() {
        return ratingId.getEstimation();
    }
}
