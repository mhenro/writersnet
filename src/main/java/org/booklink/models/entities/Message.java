package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mhenr on 06.12.2017.
 */
@Entity
@Table(name = "messages")
public class Message {
    private Long id;
    private User creator;
    private String message;
    private Date created;
    private ChatGroup group;
    private Boolean unread;

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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(name = "message_text")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
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

    @Transient
    @Deprecated
    public String getCreatorId() {
        return creator.getUsername();
    }

    @Transient
    @Deprecated
    public String getCreatorFullName() {
        return creator.getFullName();
    }

    @Transient
    @Deprecated
    public Long getChatGroupId() {
        return group.getId();
    }

    @Transient
    @Deprecated
    public String getCreatorAvatar() {
        return creator.getAvatar();
    }
}
