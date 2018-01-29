package org.booklink.models.response;

import org.booklink.models.entities.Comment;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by mhenr on 24.12.2017.
 */
public class CommentResponse {
    private Long id;
    private Long bookId;
    private String userId;
    private String userFullName;
    private String userAvatar;
    private String comment;
    private CommentResponse relatedTo;
    private LocalDateTime created;

    public CommentResponse() {}

    public CommentResponse(final Long id, final Long bookId, final String userId, final String firstName, final String lastName,
                           final String avatar, final String comment, final Comment relatedTo, final LocalDateTime created) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        if (firstName == null && lastName == null) {
            this.userFullName = "Anonymous";
        } else {
            this.userFullName = firstName + " " + lastName;
        }
        this.userAvatar = avatar;
        this.comment = comment;
        this.created = created;

        if (relatedTo != null&& relatedTo.getComment() != null && relatedTo.getCreated() != null) {
            this.relatedTo = new CommentResponse();
            this.relatedTo.setId(relatedTo.getId());
            if (relatedTo.getUser() != null) {
                this.relatedTo.setUserFullName(relatedTo.getUser().getFullName());
                this.relatedTo.setUserAvatar(relatedTo.getUser().getAvatar());
            } else {
                this.relatedTo.setUserFullName("Anonymous");
            }
            this.relatedTo.setComment(relatedTo.getComment());
            this.relatedTo.setCreated(relatedTo.getCreated());
        }
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public CommentResponse getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(CommentResponse relatedTo) {
        this.relatedTo = relatedTo;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
