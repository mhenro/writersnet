package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.*;
import org.booklink.models.Genre;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_id")
    public BookText getBookText() {
        return bookText;
    }

    public void setBookText(BookText bookText) {
        this.bookText = bookText;
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

    @Temporal(TemporalType.DATE)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author", nullable = false)
    //@JsonBackReference
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Transient
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ratingId.bookId")
    public List<Rating> getRating() {
        return rating;
    }

    public void setRating(List<Rating> rating) {
        this.rating = rating;
    }

    @Transient
    public Integer getSize() {
        if (size != null) {
            return size;
        }
        return 0;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "serie_id")
    public BookSerie getBookSerie() {
        return bookSerie;
    }

    public void setBookSerie(BookSerie bookSerie) {
        this.bookSerie = bookSerie;
    }

    @Transient
    public Long getSerieId() {
        return serieId;
    }

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
}
