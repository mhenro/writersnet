package org.booklink.models.response;

import java.util.Date;

/**
 * Created by mhenr on 08.01.2018.
 */
public class DetailedCommentResponse {
    private Long id;
    private Long bookId;
    private String bookName;
    private String bookCover;
    private String authorId;
    private String authorFullName;
    private String authorAvatar;
    private String userFullName;
    private String userAvatar;
    private String comment;
    private Date created;

    public DetailedCommentResponse(final Long id, final Long bookId, final String bookName, final String bookCover,
                                   final String authorId, final String authorFirstName, final String authorLastName,
                                   final String authorAvatar, final String userFirstName, final String userLastName,
                                   final String userAvatar, String comment, final Date created) {
        this.id = id;
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookCover = bookCover;
        this.authorId = authorId;
        this.authorFullName = authorFirstName + " " + authorLastName;
        this.authorAvatar = authorAvatar;
        this.userFullName = userFirstName == null || userLastName == null ? "Anonymous" : userFirstName + " " + userLastName;
        this.userAvatar = userAvatar;
        this.comment = comment;
        this.created = created;
    }

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

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
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

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
