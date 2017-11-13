package org.booklink.models.request_models;

import java.util.Date;

/**
 * Created by mhenr on 07.11.2017.
 */
public class BookComment {
    private Long id;
    private Long bookId;
    private String userId;
    private String comment;
    private Long relatedTo;
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(Long relatedTo) {
        this.relatedTo = relatedTo;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
