package org.booklink.models.top_models;

/**
 * Created by mhenr on 02.12.2017.
 */
public class TopAuthorBookCount {
    private String username;
    private long bookCount;

    public TopAuthorBookCount(final String username, final long bookCount) {
        this.username = username;
        this.bookCount = bookCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getBookCount() {
        return bookCount;
    }

    public void setBookCount(long bookCount) {
        this.bookCount = bookCount;
    }
}
