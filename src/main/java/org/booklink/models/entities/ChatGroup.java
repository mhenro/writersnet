package org.booklink.models.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by mhenr on 06.12.2017.
 */
@Entity
@Table(name = "chat_groups")
public class ChatGroup {
    private Long id;
    private String name;
    private String avatar;
    private User creator;
    private Date created;
    private User primaryRecipient;
    private List<Message> messages = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
    @JoinColumn(name = "primary_recipient")
    public User getPrimaryRecipient() {
        return primaryRecipient;
    }

    public void setPrimaryRecipient(User primaryRecipient) {
        this.primaryRecipient = primaryRecipient;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
