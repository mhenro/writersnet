package com.writersnets.models.entities.books;

import com.writersnets.models.Genre;
import com.writersnets.models.entities.*;
import com.writersnets.models.entities.users.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by mhenr on 30.09.2017.
 */
@Entity
@Table(name = "books")
@Audited
@Getter @Setter @NoArgsConstructor
public class Book extends AbstractIdEntity {
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    private String description;

    private LocalDate created;

    @Enumerated(EnumType.ORDINAL)
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serie_id")
    private BookSerie bookSerie;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    private String language;

    private String cover;

    @Column(nullable = false)
    private Long views = 0L;

    @Column(name = "comments_count")
    private Long commentsCount = 0L;

    @Column(name = "total_rating")
    private Long totalRating = 0L;

    @Column(name = "total_votes")
    private Long totalVotes = 0L;

    @Column(name = "review_count")
    private Long reviewCount = 0L;

    private Boolean paid;

    private Long cost;

    @OneToOne(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private BookText bookText;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ratingId.book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> rating;


    /* getters and setter */


    public void setBookText(BookText bookText) {
        this.bookText = bookText;
        if (bookText != null) {
            bookText.setBook(this);
        }
    }
}
