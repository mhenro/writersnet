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
    private AuthorResponse author;
    private Long reviewCount;
    private Boolean paid;
    private Long cost;

    public BookResponse(){}

    public BookResponse(final Long id, final Long serieId, final String serieName, final Long commentsCount, final String cover, final Date created,
                        final String description, final Genre genre, final String language, final Date lastUpdate, final String name,
                        final Integer size, final Long totalRating, final Long totalVotes, final Long views, final String username,
                        final String firstName, final String lastName, final String avatar, final Long reviewCount, final Boolean premium,
                        final Boolean paid, final Long cost) {
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
        if (totalVotes != null && totalVotes != 0) {
            this.totalRating = new TotalRating((float)totalRating / totalVotes, totalVotes);
        } else {
            this.totalRating = new TotalRating(0, 0);
        }
        this.views = views;

        this.author = new AuthorResponse();
        author.setUsername(username);
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setAvatar(avatar);
        author.setPremium(premium);

        this.reviewCount = reviewCount;
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

    public AuthorResponse getAuthor() {
        return author;
    }

    public void setAuthor(AuthorResponse author) {
        this.author = author;
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
