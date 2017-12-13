package org.booklink.models.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mhenr on 13.12.2017.
 */
@Entity
public class News {
    private Long id;
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
                        */
    private User author;
    private Book book;
    private Date created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "news_type")
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @OneToOne
    @JoinColumn(name = "author_id")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @OneToOne
    @JoinColumn(name = "book_id")
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
