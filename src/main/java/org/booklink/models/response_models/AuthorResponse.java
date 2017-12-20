package org.booklink.models.response_models;

import org.booklink.models.entities.*;
import org.booklink.models.request_models.TotalRating;
import org.booklink.models.request_models.TotalSize;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mhenr on 20.12.2017.
 */
public class AuthorResponse {
    private String username;
    private String email;
    private Date birthday;
    private String city;
    private String firstName;
    private String lastName;
    private String fullName;
    private String avatar;
    private Set<BookResponse> books;
    private Set<BookSerieResponse> bookSeries;
    private SectionResponse section;
    private String sectionDescription;
    private String sectionName;
    private String language;
    private String preferredLanguages;
    private Long views = 0L;
    private SessionResponse session;
    private TotalRating rating;
    private TotalSize totalSize;
    private Set<FriendshipResponse> subscribers;
    private Set<FriendshipResponse> subscriptions;

    public AuthorResponse() {}

    public AuthorResponse(final User user) {
        if (user == null) {
            return;
        }
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        this.city = user.getCity();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fullName = user.getFullName();
        this.avatar = user.getAvatar();
        this.sectionDescription = user.getSection().getDescription();
        this.sectionName = user.getSection().getName();
        this.language = user.getLanguage();
        this.preferredLanguages = user.getPreferredLanguages();
        this.views = user.getViews();
        this.rating = user.getRating();
        this.totalSize = user.getTotalSize();

        this.bookSeries = user.getBookSeries().stream().map(this::convertSerie).collect(Collectors.toSet());
        this.books = user.getBooks().stream().map(this::convertBook).collect(Collectors.toSet());
        this.subscribers = user.getSubscribers().stream().map(this::convertFriendship).collect(Collectors.toSet());
        this.subscriptions = user.getSubscriptions().stream().map(this::convertFriendship).collect(Collectors.toSet());
        this.section = new SectionResponse(user.getSection());
        this.session = new SessionResponse(user.getSession());
    }

    private BookSerieResponse convertSerie(final BookSerie bookSerie) {
        return new BookSerieResponse(bookSerie);
    }

    private BookResponse convertBook(final Book book) {
        return new BookResponse(book);
    }

    private FriendshipResponse convertFriendship(final Friendship friendship) {
        return new FriendshipResponse(friendship);
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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

    public Set<BookResponse> getBooks() {
        return books;
    }

    public void setBooks(Set<BookResponse> books) {
        this.books = books;
    }

    public Set<BookSerieResponse> getBookSeries() {
        return bookSeries;
    }

    public void setBookSeries(Set<BookSerieResponse> bookSeries) {
        this.bookSeries = bookSeries;
    }

    public SectionResponse getSection() {
        return section;
    }

    public void setSection(SectionResponse section) {
        this.section = section;
    }

    public String getSectionDescription() {
        return sectionDescription;
    }

    public void setSectionDescription(String sectionDescription) {
        this.sectionDescription = sectionDescription;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
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

    public SessionResponse getSession() {
        return session;
    }

    public void setSession(SessionResponse session) {
        this.session = session;
    }

    public TotalRating getRating() {
        return rating;
    }

    public void setRating(TotalRating rating) {
        this.rating = rating;
    }

    public TotalSize getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(TotalSize totalSize) {
        this.totalSize = totalSize;
    }

    public Set<FriendshipResponse> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<FriendshipResponse> subscribers) {
        this.subscribers = subscribers;
    }

    public Set<FriendshipResponse> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<FriendshipResponse> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
