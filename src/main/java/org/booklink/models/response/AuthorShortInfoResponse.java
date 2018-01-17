package org.booklink.models.response;

import org.booklink.models.request.TotalRating;

/**
 * Created by mhenr on 22.12.2017.
 */
public class AuthorShortInfoResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private String avatar;
    private String preferredLanguages;
    private Long views;
    private TotalRating rating;
    private Boolean online;
    private Boolean premium;

    public AuthorShortInfoResponse(){}

    public AuthorShortInfoResponse(final String username, final String firstName, final String lastName,
                                   final String avatar, final String preferredLanguages, final Long views,
                                   final Long totalRating, final Long totalVotes, final Boolean online,
                                   final Boolean premium) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.avatar = avatar;
        this.preferredLanguages = preferredLanguages;
        this.views = views;
        if (totalVotes != null && totalVotes != 0) {
            this.rating = new TotalRating((float)totalRating/totalVotes, totalVotes);
        } else {
            this.rating = new TotalRating(0, 0);
        }
        this.online = online;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPreferredLanguages() {
        return preferredLanguages;
    }

    public void setPreferredLanguages(String preferredLanguages) {
        this.preferredLanguages = preferredLanguages;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public TotalRating getRating() {
        return rating;
    }

    public void setRating(TotalRating rating) {
        this.rating = rating;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }
}
