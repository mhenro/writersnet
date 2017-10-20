package org.booklink.models.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * Created by mhenr on 18.10.2017.
 */
@Entity
public class Rating {
    private RatingId id;
    private Long userCount;

    @EmbeddedId
    public RatingId getId() {
        return id;
    }

    public void setId(RatingId id) {
        this.id = id;
    }

    @Column(name = "user_count")
    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }
}
