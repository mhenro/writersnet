package com.writersnets.models.top_models;

/**
 * Created by mhenr on 03.12.2017.
 */
public class TopAuthorViews {
    private String username;
    private String firstName;
    private String lastName;
    private long views;
    private Boolean premium;

    public TopAuthorViews(final String username, final String firstName, final String lastName, final long views, final Boolean premium) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.views = views;
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

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }
}
