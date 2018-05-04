package com.writersnets.models.entities;

import com.writersnets.models.Genre;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by mhenr on 30.09.2017.
 */
@Entity
public class Book extends AbstractEntity {
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
    private Long id;
    private String name;

    @OneToOne(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private BookText bookText;
    private String description;
    private LocalDate created;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Enumerated(EnumType.ORDINAL)
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ratingId.book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serie_id")
    private BookSerie bookSerie;
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
    public LocalDate getCreated() {
        return created;
    }
    public void setCreated(LocalDate created) {
        this.created = created;
    }
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public Genre getGenre() {
        return genre;
    }
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public List<Rating> getRating() {
        return rating;
    }
    public void setRating(List<Rating> rating) {
        this.rating = rating;
    }
    public BookSerie getBookSerie() {
        return bookSerie;
    }
    public void setBookSerie(BookSerie bookSerie) {
        this.bookSerie = bookSerie;
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
    public Long getViews() {
        return views;
    }
    public void setViews(Long views) {
        this.views = views;
    }
    public Long getCommentsCount() {
        return commentsCount;
    }
    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
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
    public Long getReviewCount() {
        return reviewCount;
    }
    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }
    public Boolean getPaid() {
        return paid;
    }
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
    public Long getCost() {
        return cost;
    }
    public void setCost(Long cost) {
        this.cost = cost;
    }
}
