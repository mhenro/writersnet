package org.booklink.models.entities;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by mhenr on 07.01.2018.
 */
@Embeddable
public class ReviewIP_PK implements Serializable{
    private Review review;
    private String ip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewIP_PK that = (ReviewIP_PK) o;

        if (review != null ? !review.equals(that.review) : that.review != null) return false;
        return ip != null ? ip.equals(that.ip) : that.ip == null;

    }

    @Override
    public int hashCode() {
        int result = review != null ? review.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        return result;
    }
}
