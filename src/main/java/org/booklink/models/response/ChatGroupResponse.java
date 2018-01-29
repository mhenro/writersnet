package org.booklink.models.response;

import org.booklink.models.entities.ChatGroup;
import org.booklink.models.entities.Message;
import org.booklink.models.entities.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mhenr on 07.12.2017.
 */
public class ChatGroupResponse implements Serializable {
    private long id;
    private String creatorId;
    private String creatorFullName;
    private String creatorAvatar;
    private LocalDateTime created;
    private String lastMessageText;
    private String lastMessageAvatar;
    private LocalDateTime lastMessageDate;
    private String primaryRecipientFullName;
    private String primaryRecipientAvatar;
    private String recipients;
    private Boolean unreadByCreator;
    private Boolean unreadByRecipient;

    public ChatGroupResponse(final ChatGroup group) {
        if (group == null) {
            return;
        }
        final User primaryRecipient = group.getPrimaryRecipient();
        final List<Message> messages = group.getMessages();

        this.id = group.getId();
        this.creatorId = group.getCreator().getUsername();
        this.creatorFullName = group.getCreator().getFullName();
        this.creatorAvatar = group.getCreator().getAvatar();
        this.created = group.getCreated();
        this.primaryRecipientFullName = group.getName() == null ? group.getPrimaryRecipient().getFullName() : group.getName();
        this.primaryRecipientAvatar = group.getAvatar() == null ? group.getPrimaryRecipient().getAvatar() : group.getAvatar();
        final Set<String> recipientSet = messages.stream().map(Message::getCreatorFullName).collect(Collectors.toSet());
        recipientSet.add(primaryRecipient.getFullName());
        this.recipients = recipientSet.stream().collect(Collectors.joining(", "));

        if (!messages.isEmpty()) {
            final Message lastMessage = messages.stream().max(Comparator.comparing(Message::getCreated)).get();
            this.lastMessageText = lastMessage.getMessage();
            this.lastMessageAvatar = lastMessage.getCreator().getAvatar();
            this.lastMessageDate = lastMessage.getCreated();
        }
        this.unreadByCreator = group.getUnreadByCreator();
        this.unreadByRecipient = group.getUnreadByRecipient();
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

    public String getCreatorAvatar() {
        return creatorAvatar;
    }

    public void setCreatorAvatar(String creatorAvatar) {
        this.creatorAvatar = creatorAvatar;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public String getLastMessageAvatar() {
        return lastMessageAvatar;
    }

    public void setLastMessageAvatar(String lastMessageAvatar) {
        this.lastMessageAvatar = lastMessageAvatar;
    }

    public LocalDateTime getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(LocalDateTime lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getPrimaryRecipientFullName() {
        return primaryRecipientFullName;
    }

    public void setPrimaryRecipientFullName(String primaryRecipientFullName) {
        this.primaryRecipientFullName = primaryRecipientFullName;
    }

    public String getPrimaryRecipientAvatar() {
        return primaryRecipientAvatar;
    }

    public void setPrimaryRecipientAvatar(String primaryRecipientAvatar) {
        this.primaryRecipientAvatar = primaryRecipientAvatar;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public Boolean getUnreadByCreator() {
        return unreadByCreator;
    }

    public void setUnreadByCreator(Boolean unreadByCreator) {
        this.unreadByCreator = unreadByCreator;
    }

    public Boolean getUnreadByRecipient() {
        return unreadByRecipient;
    }

    public void setUnreadByRecipient(Boolean unreadByRecipient) {
        this.unreadByRecipient = unreadByRecipient;
    }
}
