package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.booklink.models.request_models.TotalRating;
import org.booklink.models.request_models.TotalSize;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mhenr on 02.10.2017.
 */
@Entity
@Table(name = "users")
public class User {
    private String username;
    private String password;
    private Boolean enabled;
    private String activationToken;
    private String authority;
    private String email;
    private Date birthday;
    private String city;
    private String firstName;
    private String lastName;
    private String avatar;
    private Set<Book> books;
    private Set<BookSerie> bookSeries;
    private Section section;
    private String sectionDescription;
    private String sectionName;
    private String language;
    private String preferredLanguages;
    private Long views = 0L;
    private Set<Friendship> subscribers = new HashSet<>();
    private Set<Friendship> subscriptions = new HashSet<>();
    private List<ChatGroup> chatGroups = new ArrayList<>();
    private Session session;
    private Float totalRating;
    private Long totalVotes;

    @Id
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

    @Column(name = "activation_token")
    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    @Column(name = "authority")
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

    @Temporal(TemporalType.DATE)
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

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
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

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<BookSerie> getBookSeries() {
        return bookSeries;
    }

    public void setBookSeries(Set<BookSerie> bookSeries) {
        this.bookSeries = bookSeries;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "section_id")
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

    @Column(name = "preferred_languages")
    public String getPreferredLanguages() {
        return preferredLanguages;
    }

    public void setPreferredLanguages(String preferredLanguages) {
        this.preferredLanguages = preferredLanguages;
    }

    public Long getViews() {
        return views;
    }

    @Column(nullable = false)
    public void setViews(Long views) {
        this.views = views;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "friendshipPK.subscriber")
    public Set<Friendship> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Friendship> subscribers) {
        this.subscribers = subscribers;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "friendshipPK.subscription")
    public Set<Friendship> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Friendship> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "chat_groups_users",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id")
    )
    public List<ChatGroup> getChatGroups() {
        return chatGroups;
    }

    public void setChatGroups(List<ChatGroup> chatGroups) {
        this.chatGroups = chatGroups;
    }

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Column(name = "total_rating", precision = 2, scale=1)
    public Float getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Float totalRating) {
        this.totalRating = totalRating;
    }

    @Column(name = "total_votes")
    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    /* -----------------------------business logic-------------------------------------------------------- */

    @Transient
    @Deprecated
    public String getSectionName() {
        return sectionName;
    }

    @Transient
    @Deprecated
    public String getSectionDescription() {
        return sectionDescription;
    }

    /* method for calculating total rating of the author */
    @Transient
    public TotalRating getRating() {
        TotalRating authorRating = new TotalRating();
        if (books == null) {
            return authorRating;
        }
        long totalUsers = books.stream().map(book -> book.getTotalRating().getUserCount()).collect(Collectors.summingLong(n -> n));
        Map<Integer, Long> countByStars = books.stream().flatMap(book -> book.getRating().stream())
                .collect(Collectors.groupingBy(Rating::getEstimation, Collectors.counting()));
        float averageRating = (float)countByStars.entrySet().stream()
                .map(map -> map.getKey() * map.getValue().intValue())
                .collect(Collectors.summingInt(n -> n)) / totalUsers;

        if (totalUsers == 0) {
            return authorRating;
        }
        authorRating.setUserCount(totalUsers);
        authorRating.setAverageRating(averageRating);

        return authorRating;
    }

    @Transient
    public TotalSize getTotalSize() {
        TotalSize totalSize = new TotalSize();
        long size = Optional.ofNullable(books)
                .map(books -> books.stream()
                        .map(book -> book.getSize())
                        .collect(Collectors.summingLong(n -> n)))
                .orElse(0L);
        long booksCount = Optional.ofNullable(books)
                .map(books -> books.size())
                .orElse(0);
        totalSize.setTotalSize(size);
        totalSize.setTotalBooks(booksCount);
        return totalSize;
    }

    @Transient
    @JsonIgnore
    public boolean isSubscriberOf(final String anotherUser) {
        return getSubscribers().stream()
                .filter(subscriber -> subscriber.getFriendshipPK().getSubscription().getUsername().equals(anotherUser))
                .findAny()
                .isPresent();
    }

    @Transient
    @JsonIgnore
    public boolean isSubscriptionOf(final String anotherUser) {
        return getSubscriptions().stream()
                .filter(subscription -> subscription.getFriendshipPK().getSubscriber().getUsername().equals(anotherUser))
                .findAny()
                .isPresent();
    }

    @Transient
    @JsonIgnore
    public boolean isFriendOf(final String anotherUser) {
        return isSubscriberOf(anotherUser) && isSubscriptionOf(anotherUser);
    }

    @Transient
    @JsonIgnore
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
