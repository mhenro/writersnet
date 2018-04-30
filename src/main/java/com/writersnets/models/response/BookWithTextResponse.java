package com.writersnets.models.response;

import com.writersnets.models.Genre;
import com.writersnets.models.entities.Book;
import com.writersnets.models.entities.BookText;
import com.writersnets.models.request.TotalRating;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by mhenr on 21.12.2017.
 */
public class BookWithTextResponse {
    private Long id;
    private BookSerieResponse bookSerie;
    private String authorId;
    private String authorFullName;
    private Long commentsCount;
    private String cover;
    private LocalDate created;
    private String description;
    private Genre genre;
    private String language;
    private LocalDateTime lastUpdate;
    private String name;
    private Integer size;
    private TotalRating totalRating;
    private Long views;
    private BookTextResponse bookText;
    private Boolean paid;
    private Long cost;

    private Integer totalPages;

    public BookWithTextResponse(){}

    public BookWithTextResponse(final Long id, final Long serieId, final String serieName, final String authorId,
                                final String authorFirstName, final String authorLastName, final Long commentsCount,
                                final String cover, final LocalDate created, final String description, final Genre genre,
                                final String language, final LocalDateTime lastUpdate, final String name, final Integer size,
                                final Long totalRating, final Long totalVotes, final Long views, final Long textId,
                                final String text, final String prevText, final Boolean paid, final Long cost) {
        this.id = id;
        if (serieId == null && serieName == null) {
            this.bookSerie = null;
        } else {
            this.bookSerie = new BookSerieResponse(serieId, serieName);
        }
        this.authorId = authorId;
        this.authorFullName = authorFirstName + " " + authorLastName;
        this.commentsCount = commentsCount;
        this.cover = cover;
        this.created = created;
        this.description = description;
        this.genre = genre;
        this.language = language;
        this.lastUpdate = lastUpdate;
        this.name = name;
        this.size = size;
        if (totalVotes != null && totalVotes != 0) {
            this.totalRating = new TotalRating((float)totalRating / totalVotes, totalVotes);
        } else {
            this.totalRating = new TotalRating(0, 0);
        }
        this.views = views;
        this.bookText = new BookTextResponse(textId, text, prevText);
        this.totalPages = 1;
        this.paid = paid;
        this.cost = cost;
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
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

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
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

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
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

    public BookTextResponse getBookText() {
        return bookText;
    }

    public void setBookText(BookTextResponse bookText) {
        this.bookText = bookText;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
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
