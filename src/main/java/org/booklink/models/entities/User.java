package org.booklink.models.entities;

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
@SecondaryTable(name = "authorities", pkJoinColumns = @PrimaryKeyJoinColumn(name = "username", referencedColumnName = "username"))
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="username")
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
    private Section section;
    private String sectionDescription;
    private String sectionName;
    private String language;
    private String preferredLanguages;
    private Long views = 0L;
    private Set<Friendship> subscribers = new HashSet<>();
    private Set<Friendship> subscriptions = new HashSet<>();

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

    @Column(table = "authorities", name = "authority")
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "author")
   // @JsonManagedReference
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "author")
    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @Transient
    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    @Transient
    public String getSectionDescription() {
        return sectionDescription;
    }

    public void setSectionDescription(String sectionDescription) {
        this.sectionDescription = sectionDescription;
    }

    /* method for calculating total rating of the author */
    @Transient
    public TotalRating getRating() {
        TotalRating authorRating = new TotalRating();
        if (books == null) {
            return authorRating;
        }
        int totalUsers = books.stream().map(book -> book.getTotalRating().getUserCount()).collect(Collectors.summingInt(n -> n));
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
        int size = Optional.ofNullable(books)
                .map(books -> books.stream()
                        .map(book -> book.getSize())
                        .collect(Collectors.summingInt(n -> n)))
                .orElse(0);
        int booksCount = Optional.ofNullable(books)
                .map(books -> books.size())
                .orElse(0);
        totalSize.setTotalSize(size);
        totalSize.setTotalBooks(booksCount);
        return totalSize;
    }

    @Transient
    public Set<BookSerie> getBookSeries() {
        return Optional.ofNullable(books)
                .map(books -> books.stream()
                        .map(book -> book.getBookSerie())
                        .filter(book -> book != null)
                        .collect(Collectors.toSet()))
                .orElseGet(() -> Collections.emptySet());
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscriber")
    public Set<Friendship> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Friendship> subscribers) {
        this.subscribers = subscribers;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscription")
    public Set<Friendship> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Friendship> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
