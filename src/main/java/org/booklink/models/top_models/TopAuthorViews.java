package org.booklink.models.top_models;

/**
 * Created by mhenr on 03.12.2017.
 */
public class TopAuthorViews {
    private String username;
    private String firstName;
    private String lastName;
    private long views;

    public TopAuthorViews(final String username, final String firstName, final String lastName, final long views) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.views = views;
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

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
