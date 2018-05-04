package com.writersnets.models.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 06.12.2017.
 */
@Entity
@Table(name = "messages")
public class Message extends AbstractEntity {
    @GenericGenerator(
            name = "message_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "messages_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_generator")
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "message_text")
    private String message;
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private ChatGroup group;
    private Boolean unread;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getCreator() {
        return creator;
    }
    public void setCreator(User creator) {
        this.creator = creator;
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
    public ChatGroup getGroup() {
        return group;
    }
    public void setGroup(ChatGroup group) {
        this.group = group;
    }
    public Boolean getUnread() {
        return unread;
    }
    public void setUnread(Boolean unread) {
        this.unread = unread;
    }

    /* --------------------------------------------business logic-------------------------------------------- */

    public String getCreatorFullName() {
        return creator.getFullName();
    }
}
