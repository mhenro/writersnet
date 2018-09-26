package com.writersnets.models.response;

import com.writersnets.models.entities.users.Section;
import com.writersnets.models.request.TotalRating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Created by mhenr on 20.12.2017.
 */
@Getter @Setter @NoArgsConstructor
public class AuthorResponse {
    private String username;
    private String email;
    private LocalDate birthday;
    private String city;
    private String firstName;
    private String lastName;
    private String fullName;
    private String avatar;
    private SectionResponse section;
    private String language;
    private String preferredLanguages;
    private Long views = 0L;
    private Long complaints = 0L;
    private TotalRating rating;
    private Boolean online;
    private Boolean premium;

    public AuthorResponse(final String username, final String email, final LocalDate birthday, final String city, final String firstName,
                          final String lastName, final String avatar, final Section section,
                          final String language, final String preferredLanguages, final Long views, final Long complaints,
                          final Long totalRating, final Long totalVotes, final Boolean online, final Boolean premium) {
        this.username = username;
        this.email = email;
        this.birthday = birthday;
        this.city = city;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.avatar = avatar;
        this.section = new SectionResponse(section);
        this.language = language;
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
