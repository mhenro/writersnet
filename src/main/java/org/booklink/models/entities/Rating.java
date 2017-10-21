package org.booklink.models.entities;

import javax.persistence.*;

/**
 * Created by mhenr on 18.10.2017.
 */
@Entity
public class Rating {
    private RatingId ratingId;
    private Integer userCount;

    @EmbeddedId
    public RatingId getRatingId() {
        return ratingId;
    }

    public void setRatingId(RatingId ratingId) {
        this.ratingId = ratingId;
    }

    @Column(name = "user_count")
    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}
