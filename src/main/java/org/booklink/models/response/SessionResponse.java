package org.booklink.models.response;

import org.booklink.models.entities.Session;

import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
public class SessionResponse {
    private long id;
    private String author;
    private Date expired;

    public SessionResponse(final Session session) {
        if (session == null) {
            return;
        }
        this.id = session.getId();
        this.author = session.getAuthor().getUsername();
        this.expired = session.getExpired();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
}
