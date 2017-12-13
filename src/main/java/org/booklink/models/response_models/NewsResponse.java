package org.booklink.models.response_models;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.User;

import java.util.Date;

/**
 * Created by mhenr on 13.12.2017.
 */
public class NewsResponse {
    private Long id;
    private Long type;
    private String authorId;
    private String authorFullName;
    private String authorAvatar;
    private Long bookId;
    private String bookName;
    private Date created;

    public NewsResponse(final Long id, final Long type, final User author, final Book book, final Date created) {
        this.id = id;
        this.type = type;
        this.authorId = author.getUsername();
        this.authorFullName = author.getFullName();
        this.authorAvatar = author.getAvatar();
        this.bookId = book.getId();
        this.bookName = book.getName();
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
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

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
