package com.writersnets.models.response;

import com.writersnets.models.request.TotalRating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by mhenr on 22.12.2017.
 */
@Getter @Setter @NoArgsConstructor
public class AuthorShortInfoResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private String avatar;
    private String preferredLanguages;
    private Long views;
    private Long complaints;
    private TotalRating rating;
    private Boolean online;
    private Boolean premium;

    public AuthorShortInfoResponse(final String username, final String firstName, final String lastName,
                                   final String avatar, final String preferredLanguages, final Long views,
                                   final Long complaints, final Long totalRating, final Long totalVotes,
                                   final Boolean online, final Boolean premium) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.avatar = avatar;
        this.preferredLanguages = preferredLanguages;
        this.views = views;
        this.complaints = complaints;
        if (totalVotes != null && totalVotes != 0) {
            this.rating = new TotalRating((float)totalRating/totalVotes, totalVotes);
        } else {
            this.rating = new TotalRating(0, 0);
        }
        this.online = online;
        this.premium = premium;
    }
}
