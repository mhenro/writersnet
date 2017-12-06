package org.booklink.models.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mhenr on 06.12.2017.
 */
@Entity
@Table(name = "chat_groups")
public class ChatGroups {
    private long id;
    private User creator;
    private Date created;

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
