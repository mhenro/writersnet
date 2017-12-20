package org.booklink.models.response_models;

import org.booklink.models.Genre;
import org.booklink.models.entities.Book;
import org.booklink.models.entities.BookText;
import org.booklink.models.request_models.TotalRating;

import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
public class BookResponse {
    private long id;
    private BookSerieResponse bookSerie;
    private long commentsCount;
    private String cover;
    private Date created;
    private String description;
    private Genre genre;
    private String language;
    private Date lastUpdate;
    private String name;
    private long size;
    private TotalRating totalRating;
    private long views;

    public BookResponse(){}

    public BookResponse(final Book book) {
        if (book == null) {
            return;
        }
        this.id = book.getId();
        this.commentsCount = book.getCommentsCount();
        this.cover = book.getCover();
        this.created = book.getCreated();
        this.description = book.getDescription();
        this.genre = book.getGenre();
        this.language = book.getLanguage();
        this.lastUpdate = book.getLastUpdate();
        this.name = book.getName();
        this.size = book.getSize();
        this.totalRating = book.getTotalRating();
        this.views = book.getViews();

        if (book.getBookSerie() != null) {
            this.bookSerie = new BookSerieResponse();
            this.bookSerie.setId(book.getBookSerie().getId());
            this.bookSerie.setName(book.getBookSerie().getName());
            this.bookSerie.setUserId(book.getBookSerie().getUserId());
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BookSerieResponse getBookSerie() {
        return bookSerie;
    }

    public void setBookSerie(BookSerieResponse bookSerie) {
        this.bookSerie = bookSerie;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public TotalRating getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(TotalRating totalRating) {
        this.totalRating = totalRating;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
