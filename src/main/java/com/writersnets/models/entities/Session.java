package com.writersnets.models.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 20.12.2017.
 */
@Entity
@Table(name = "sessions")
public class Session extends AbstractEntity {
    @GenericGenerator(
            name = "session_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "session_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_generator")
    @Column(updatable = false, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User author;

    @Column(name = "expire_date")
    private LocalDateTime expired;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public LocalDateTime getExpired() {
        return expired;
    }
    public void setExpired(LocalDateTime expired) {
        this.expired = expired;
    }
}
