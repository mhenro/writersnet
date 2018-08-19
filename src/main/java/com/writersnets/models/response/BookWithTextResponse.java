package com.writersnets.models.response;

import com.writersnets.models.Genre;
import com.writersnets.models.request.TotalRating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 21.12.2017.
 */
@Setter
@Getter
@NoArgsConstructor
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
}
