package com.writersnets.models.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 13.12.2017.
 */
@Entity
public class News extends AbstractEntity {
    @GenericGenerator(
            name = "news_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "news_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_generator")
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "news_type")
    private Long type;  /*
                            0 - book was created
                            1 - book was updated
                            2 - book was deleted
                            3 - leave a comment to book
                            4 - author personal info was updated
                            5 - new serie was added
                            6 - serie was updated
                            7 - serie was deleted
                            8 - friend was added
                            9 - friend was removed
                            10 - contest was created
                        */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private User subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getType() {
        return type;
    }
    public void setType(Long type) {
        this.type = type;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public LocalDateTime getCreated() {
        return created;
    }
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    public User getSubscription() {
        return subscription;
    }
    public void setSubscription(User subscription) {
        this.subscription = subscription;
    }
    public Contest getContest() {
        return contest;
    }
    public void setContest(Contest contest) {
        this.contest = contest;
    }
}
