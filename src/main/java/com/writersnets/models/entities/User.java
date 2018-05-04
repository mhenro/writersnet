package com.writersnets.models.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mhenr on 02.10.2017.
 */
@Entity
@Table(name = "users")
public class User extends AbstractEntity {
    @Id
    private String username;
    private String password;
    private Boolean enabled;

    @Column(name = "activation_token")
    private String activationToken;

    @Column(name = "authority")
    private String authority;
    private String email;
    private LocalDate birthday;
    private String city;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    private String avatar;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "section_id")
    private Section section;
    private String language;

    @Column(name = "preferred_languages")
    private String preferredLanguages;

    @Column(nullable = false)
    private Long views = 0L;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "chat_groups_users",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id")
    )
    private List<ChatGroup> chatGroups = new ArrayList<>();

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Session session;

    @Column(name = "total_rating")
    private Long totalRating = 0L;

    @Column(name = "total_votes")
    private Long totalVotes = 0L;

    @Column(name = "comments_count")
    private Long commentsCount = 0L;
    private Boolean online;
    private Boolean premium;

    @Column(name = "premium_expired")
    private LocalDateTime premiumExpired;
    private Long balance = 0L;


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public String getActivationToken() {
        return activationToken;
    }
    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }
    public String getAuthority() {
        return authority;
    }
    public void setAuthority(String authority) {
        this.authority = authority;
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
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public Set<Book> getBooks() {
        return books;
    }
    public void setBooks(Set<Book> books) {
        this.books = books;
    }
    public Section getSection() {
        return section;
    }
    public void setSection(Section section) {
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
    public List<ChatGroup> getChatGroups() {
        return chatGroups;
    }
    public void setChatGroups(List<ChatGroup> chatGroups) {
        this.chatGroups = chatGroups;
    }
    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session = session;
    }
    public Long getTotalRating() {
        return totalRating;
    }
    public void setTotalRating(Long totalRating) {
        this.totalRating = totalRating;
    }
    public Long getTotalVotes() {
        return totalVotes;
    }
    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }
    public Long getCommentsCount() {
        return commentsCount;
    }
    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
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
    public LocalDateTime getPremiumExpired() {
        return premiumExpired;
    }
    public void setPremiumExpired(LocalDateTime premiumExpired) {
        this.premiumExpired = premiumExpired;
    }
    public Long getBalance() {
        return balance;
    }
    public void setBalance(Long balance) {
        this.balance = balance;
    }

    /* -----------------------------business logic-------------------------------------------------------- */

    public void refreshRatingFromBooks() {
        final Long totalRating = books.stream().map(Book::getTotalRating).collect(Collectors.summingLong(n -> n));
        final Long totalVotes = books.stream().map(Book::getTotalVotes).collect(Collectors.summingLong(n -> n));
        this.totalRating = totalRating;
        this.totalVotes = totalVotes;
    }

    public void refreshCommentsCountFromBooks() {
        final long commentsCount = books.stream().map(Book::getCommentsCount).collect(Collectors.summingLong(n -> n));
        this.commentsCount = commentsCount;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
