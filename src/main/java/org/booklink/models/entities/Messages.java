package org.booklink.models.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mhenr on 06.12.2017.
 */
@Entity
@Table(name = "messages")
public class Messages {
    private long id;
    private User creator;
    private String message;
    private Date created;
    private ChatGroups group;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @OneToOne
    @JoinColumn(name = "group_id")
    public ChatGroups getGroup() {
        return group;
    }

    public void setGroup(ChatGroups group) {
        this.group = group;
    }
}
