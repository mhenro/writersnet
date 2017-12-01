package org.booklink.models.request_models;

/**
 * Created by mhenr on 01.12.2017.
 */
public class BookRequest {
    private long id;
    private String name;
    private long totalRating;
    private long count;

    public BookRequest(final long id, final String name, final long totalRating, final long count) {
        this.id = id;
        this.name = name;
        this.totalRating = totalRating;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(long totalRating) {
        this.totalRating = totalRating;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
