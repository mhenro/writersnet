package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
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

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIgnore
    public ChatGroup getGroup() {
        return group;
    }

    public void setGroup(ChatGroup group) {
        this.group = group;
    }

    /* --------------------------------------------business logic-------------------------------------------- */

    @Transient
    public String getCreatorId() {
        return creator.getUsername();
    }

    @Transient
    public String getCreatorFullName() {
        return creator.getFullName();
    }

    @Transient
    public Long getChatGroupId() {
        return group.getId();
    }
}
