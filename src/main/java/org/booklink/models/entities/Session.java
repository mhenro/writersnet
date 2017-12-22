package org.booklink.models.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
@Entity
@Table(name = "sessions")
public class Session {
    private Long id;
    private User author;
    private Date expired;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expire_date")
    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
}
