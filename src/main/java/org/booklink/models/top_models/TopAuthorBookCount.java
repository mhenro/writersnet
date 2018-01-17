package org.booklink.models.top_models;

/**
 * Created by mhenr on 02.12.2017.
 */
public class TopAuthorBookCount {
    private String username;
    private String firstName;
    private String lastName;
    private long bookCount;
    private Boolean premium;

    public TopAuthorBookCount(final String username, final String firstName, final String lastName, final long bookCount,
                              final Boolean premium) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookCount = bookCount;
        this.premium = premium;
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

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }
}
