package org.booklink.models.top_models;

/**
 * Created by mhenr on 02.12.2017.
 */
public class TopAuthorBookCount {
    private String username;
    private String firstName;
    private String lastName;
    private long bookCount;

    public TopAuthorBookCount(final String username, final String firstName, final String lastName, final long bookCount) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookCount = bookCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getBookCount() {
        return bookCount;
    }

    public void setBookCount(long bookCount) {
        this.bookCount = bookCount;
    }
}
