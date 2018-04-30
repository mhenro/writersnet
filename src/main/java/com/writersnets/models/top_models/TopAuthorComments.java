package com.writersnets.models.top_models;

/**
 * Created by mhenr on 03.12.2017.
 */
public class TopAuthorComments {
    private String username;
    private String firstName;
    private String lastName;
    private long commentsCount;
    private Boolean premium;

    public TopAuthorComments(final String username, final String firstName, final String lastName, final long commentsCount,
                             final Boolean premium) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.commentsCount = commentsCount;
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

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }
}
