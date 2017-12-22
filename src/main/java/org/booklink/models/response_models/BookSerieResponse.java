package org.booklink.models.response_models;

import org.booklink.models.entities.BookSerie;

/**
 * Created by mhenr on 20.12.2017.
 */
public class BookSerieResponse {
    private long id;
    private String name;
    private String userId;

    public BookSerieResponse(){}

    public BookSerieResponse(final BookSerie bookSerie) {
        if (bookSerie == null) {
            return;
        }
        this.id = bookSerie.getId();
        this.name = bookSerie.getName();
        this.userId = bookSerie.getAuthor().getUsername();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
