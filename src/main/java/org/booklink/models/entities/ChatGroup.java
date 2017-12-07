package org.booklink.models.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mhenr on 06.12.2017.
 */
@Entity
@Table(name = "chat_groups")
public class ChatGroup {
    private Long id;
    private User creator;
    private Date created;
    private Message lastMessage;
    private List<Message> messages = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "creator_id")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @OneToOne
    @JoinColumn(name = "last_message")
    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /* --------------------------------------------------business logic---------------------------------------- */
    @Transient
    public void addNewMessage(final Message message) {
        getMessages().add(message);
        setLastMessage(message);
    }
}
