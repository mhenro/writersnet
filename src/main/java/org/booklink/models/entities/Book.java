package org.booklink.models.entities;

import org.booklink.models.Genre;
import org.booklink.models.request.TotalRating;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by mhenr on 30.09.2017.
 */
@Entity
public class Book {
    private Long id;
    private String name;
    private BookText bookText;
    private String description;
    private Date created;
    private Date lastUpdate;
    private Genre genre;
    private User author;
    private String authorName;
    private List<Rating> rating;
    private BookSerie bookSerie;
    private Long serieId;
    private String language;
    private String cover;
    private Integer size;
    private Long views = 0L;
    private Long commentsCount = 0L;
    private Long totalRating = 0L;
    private Long totalVotes = 0L;
    private Long reviewCount = 0L;

    @GenericGenerator(
            name = "book_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "book_id_seq"),
                    @Parameter(name = "initial_value", value = "0"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator")
    @Column(updatable = false, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public BookText getBookText() {
        return bookText;
    }

    public void setBookText(BookText bookText) {
        this.bookText = bookText;
        if (bookText != null) {
            bookText.setBook(this);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.DATE)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Enumerated(EnumType.ORDINAL)
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Transient
    @Deprecated
    public String getAuthorName() {
        return authorName;
    }

    @Deprecated
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ratingId.book", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Rating> getRating() {
        return rating;
    }

    public void setRating(List<Rating> rating) {
        this.rating = rating;
    }

    @Transient
    @Deprecated
    public long getSize() {
        return Optional.ofNullable(bookText)
                .flatMap(text -> Optional.ofNullable(text.getText()))
                .map(text -> text.length())
                .orElse(0);
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serie_id")
    public BookSerie getBookSerie() {
        return bookSerie;
    }

    public void setBookSerie(BookSerie bookSerie) {
        this.bookSerie = bookSerie;
    }

    @Transient
    @Deprecated
    public Long getSerieId() {
        return serieId;
    }

    @Deprecated
    public void setSerieId(Long serieId) {
        this.serieId = serieId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Column(nullable = false)
    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    @Column(name = "comments_count")
    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
    }

    @Column(name = "total_rating")
    public Long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Long totalRating) {
        this.totalRating = totalRating;
    }

    @Column(name = "total_votes")
    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    @Column(name = "review_count")
    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }
}
