package com.writersnets.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mhenr on 02.10.2017.
 */
@Entity
@Table(name = "users")
@Audited
@Getter @Setter @NoArgsConstructor
public class User extends AbstractEntity {
    @Id
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Boolean enabled;

    private String activationToken;

    private String authority;

    @NotNull
    private String email;

    private LocalDate birthday;

    private String city;

    private String firstName;

    private String lastName;

    private String avatar;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private Section section;

    private String language;

    private String preferredLanguages;

    @NotNull
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

    private Long totalRating = 0L;

    private Long totalVotes = 0L;

    @NotNull
    private Long commentsCount = 0L;

    @NotNull
    private Boolean online;

    @NotNull
    private Boolean premium;

    private LocalDateTime premiumExpired;

    @NotNull
    private Long balance = 0L;

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
