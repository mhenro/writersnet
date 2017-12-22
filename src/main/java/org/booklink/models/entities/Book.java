package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.*;
import org.booklink.models.Genre;
import org.booklink.models.request_models.TotalRating;

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
    private List<BookComments> comments;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public long getSize() {
        return Optional.ofNullable(bookText)
                .flatMap(text -> Optional.ofNullable(text.getText()))
                .map(text -> text.length())
                .orElse(0);
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<BookComments> getComments() {
        return comments;
    }

    public void setComments(List<BookComments> comments) {
        this.comments = comments;
    }

    /* -----------------------------------------business logic----------------------------------------- */

    @Transient
    public TotalRating getTotalRating() {
        if (rating == null) {
            return null;
        }
        Long totalUsers = rating.stream().filter(rating -> rating.getRatingId().getBook().getId() == id).count();
        Map<Integer, Long> countByStars = rating.stream()
                .filter(rating -> rating.getRatingId().getBook().getId() == id)
                .collect(Collectors.groupingBy(Rating::getEstimation, Collectors.counting()));
        float averageRating = (float)countByStars.entrySet().stream()
                .map(map -> map.getKey() * map.getValue().intValue())
                .collect(Collectors.summingInt(n -> n)) / totalUsers;
        TotalRating totalRating = new TotalRating();
        totalRating.setUserCount(totalUsers.intValue());
        totalRating.setAverageRating(totalUsers > 0 ? averageRating : 0);
        return totalRating;
    }
}
