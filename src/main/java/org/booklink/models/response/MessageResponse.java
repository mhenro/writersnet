package org.booklink.models.response;

import org.booklink.controllers.MessageController;
import org.booklink.models.entities.Book;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by mhenr on 07.12.2017.
 */
public class MessageResponse implements Serializable {
    private Long id;
    private String creatorId;
    private String creatorFullName;
    private String creatorAvatar;
    private String message;
    private LocalDateTime created;
    private Long groupId;
    private Boolean unread;

    public MessageResponse(final Long id, final String creatorId, final String creatorFirstName, final String creatorLastName, final String creatorAvatar,
                           final String message, final LocalDateTime created, final Long groupId, final Boolean unread) {
        this.id = id;
        this.creatorId = creatorId;
        this.creatorFullName = creatorFirstName + " " + creatorLastName;
        this.creatorAvatar = creatorAvatar;
        this.message = message;
        this.created = created;
        this.groupId = groupId;
        this.unread = unread;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public String getCreatorAvatar() {
        return creatorAvatar;
    }

    public void setCreatorAvatar(String creatorAvatar) {
        this.creatorAvatar = creatorAvatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }
}
