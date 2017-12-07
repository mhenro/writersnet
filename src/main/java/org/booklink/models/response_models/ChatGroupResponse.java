package org.booklink.models.response_models;

import org.booklink.models.entities.Message;
import org.booklink.models.entities.User;
import org.booklink.models.entities.UserChatGroup;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mhenr on 07.12.2017.
 */
public class ChatGroupResponse implements Serializable {
    private long id;
    private String creatorId;
    private String creatorFullName;
    private Date created;
    private String lastMessage;
    private Set<String> recipients;

    public ChatGroupResponse(final long id, final User creatorId, final Date created, final Message message, final UserChatGroup group) {
        this.id = id;
        this.creatorId = creatorId.getUsername();
        this.creatorFullName = creatorId.getFullName();
        this.created = created;
        this.recipients = group.getUserChatGroupPK().getGroup().getMessages().stream().map(msg -> msg.getCreatorFullName()).collect(Collectors.toSet());
        this.lastMessage = Optional.ofNullable(message).map(msg -> msg.getMessage()).orElse(null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Set<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<String> recipients) {
        this.recipients = recipients;
    }
}
