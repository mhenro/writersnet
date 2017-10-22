package org.booklink.models;

/**
 * Created by mhenr on 21.10.2017.
 */
public class TotalRating {
    private float averageRating;
    private int userCount;

    public TotalRating() {
        averageRating = 0;
        userCount = 0;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
