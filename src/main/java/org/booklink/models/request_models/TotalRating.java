package org.booklink.models.request_models;

/**
 * Created by mhenr on 21.10.2017.
 */
public class TotalRating {
    private float averageRating;
    private long userCount;

    public TotalRating() {
        averageRating = 0;
        userCount = 0;
    }

    public TotalRating(float averageRating, long userCount) {
        this.averageRating = averageRating;
        this.userCount = userCount;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }
}
