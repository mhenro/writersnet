package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.booklink.models.TotalRating;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
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
    private byte[] avatar;
    private Set<Book> books;
    private Section section;

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

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
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

    /* method for calculating total rating of the author */
    /* ratintg : int1 - estimation, int2 - user count */
    @Transient
    //@JsonIgnore
    public TotalRating getRating() {
        int totalUserCount5 = books.stream().flatMap(book -> book.getRating().stream())
                .filter(rating -> rating.getRatingId().getEstimation() == 5)
                .map(Rating::getUserCount)
                .collect(Collectors.summingInt(n -> n));
        int totalUserCount4 = books.stream().flatMap(book -> book.getRating().stream())
                .filter(rating -> rating.getRatingId().getEstimation() == 4)
                .map(Rating::getUserCount)
                .collect(Collectors.summingInt(n -> n));
        int totalUserCount3 = books.stream().flatMap(book -> book.getRating().stream())
                .filter(rating -> rating.getRatingId().getEstimation() == 3)
                .map(Rating::getUserCount)
                .collect(Collectors.summingInt(n -> n));
        int totalUserCount2 = books.stream().flatMap(book -> book.getRating().stream())
                .filter(rating -> rating.getRatingId().getEstimation() == 2)
                .map(Rating::getUserCount)
                .collect(Collectors.summingInt(n -> n));
        int totalUserCount1 = books.stream().flatMap(book -> book.getRating().stream())
                .filter(rating -> rating.getRatingId().getEstimation() == 1)
                .map(Rating::getUserCount)
                .collect(Collectors.summingInt(n -> n));
        int totalUsers = totalUserCount1 + totalUserCount2 + totalUserCount3 + totalUserCount4 + totalUserCount5;
        float avgRating = (float)(totalUserCount1 + 2*totalUserCount2 + 3*totalUserCount3 + 4*totalUserCount4 + 5*totalUserCount5) / totalUsers;
        if (totalUsers == 0) {
            avgRating = 0;
        }
        TotalRating authorRating = new TotalRating();
        authorRating.setAverageRating(avgRating);
        authorRating.setUserCount(totalUsers);

        return authorRating;
    }
}
