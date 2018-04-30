package com.writersnets.models.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private String name;
    private String avatar;
    private User creator;
    private LocalDateTime created;
    private User primaryRecipient;
    private List<Message> messages = new ArrayList<>();
    private List<User> users;
    private Boolean unreadByCreator;
    private Boolean unreadByRecipient;

    @GenericGenerator(
            name = "chat_group_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "chat_group_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_group_generator")
    @Column(updatable = false, nullable = false)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_recipient")
    public User getPrimaryRecipient() {
        return primaryRecipient;
    }

    public void setPrimaryRecipient(User primaryRecipient) {
        this.primaryRecipient = primaryRecipient;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @ManyToMany(mappedBy = "chatGroups", fetch = FetchType.LAZY)
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Column(name = "unread_by_creator")
    public Boolean getUnreadByCreator() {
        return unreadByCreator;
    }

    public void setUnreadByCreator(Boolean unreadByCreator) {
        this.unreadByCreator = unreadByCreator;
    }

    @Column(name = "unread_by_recipient")
    public Boolean getUnreadByRecipient() {
        return unreadByRecipient;
    }

    public void setUnreadByRecipient(Boolean unreadByRecipient) {
        this.unreadByRecipient = unreadByRecipient;
    }
}
