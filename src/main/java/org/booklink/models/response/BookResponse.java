package org.booklink.models.response;

import org.booklink.models.Genre;
import org.booklink.models.entities.Book;
import org.booklink.models.request.TotalRating;

import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
public class BookResponse {
    private Long id;
    private BookSerieResponse bookSerie;
    private Long commentsCount;
    private String cover;
    private Date created;
    private String description;
    private Genre genre;
    private String language;
    private Date lastUpdate;
    private String name;
    private Integer size;
    private TotalRating totalRating;
    private Long views;

    public BookResponse(){}

    public BookResponse(final Long id, final Long serieId, final String serieName, final Long commentsCount, final String cover, final Date created,
                        final String description, final Genre genre, final String language, final Date lastUpdate, final String name,
                        final Integer size, final Float totalRating, final Long totalVotes, final Long views) {
        this.id = id;
        if (serieId == null && serieName == null) {
            this.bookSerie = null;
        } else {
            this.bookSerie = new BookSerieResponse(serieId, serieName);
        }
        this.commentsCount = commentsCount;
        this.cover = cover;
        this.created = created;
        this.description = description;
        this.genre = genre;
        this.language = language;
        this.lastUpdate = lastUpdate;
        this.name = name;
        this.size = size != null ? size : 0;
        this.totalRating = new TotalRating(totalRating, totalVotes);
        this.views = views;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookSerieResponse getBookSerie() {
        return bookSerie;
    }

    public void setBookSerie(BookSerieResponse bookSerie) {
        this.bookSerie = bookSerie;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public TotalRating getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(TotalRating totalRating) {
        this.totalRating = totalRating;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }
}
