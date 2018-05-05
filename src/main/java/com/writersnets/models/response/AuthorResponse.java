package com.writersnets.models.response;

import com.writersnets.models.entities.*;
import com.writersnets.models.request.TotalRating;
import com.writersnets.models.request.TotalSize;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by mhenr on 20.12.2017.
 */
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
    private TotalRating rating;
    private Boolean online;
    private Boolean premium;

    public AuthorResponse() {}

    public AuthorResponse(final String username, final String email, final LocalDate birthday, final String city, final String firstName,
                          final String lastName, final String avatar, final Section section,
                          final String language, final String preferredLanguages, final Long views, final Long totalRating,
                          final Long totalVotes, final Boolean online, final Boolean premium) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public SectionResponse getSection() {
        return section;
    }

    public void setSection(SectionResponse section) {
        this.section = section;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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