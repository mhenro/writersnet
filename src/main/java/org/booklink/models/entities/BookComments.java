package org.booklink.models.entities;

import org.booklink.models.request.AuthorInfo;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by mhenr on 06.11.2017.
 */
@Entity
@Table(name = "book_comments")
public class BookComments {
    private Long id;
    private Book book;
    private User user;
    private String comment;
    private BookComments relatedTo;
    private Date created;
    private AuthorInfo authorInfo = new AuthorInfo();

    @GenericGenerator(
            name = "book_comment_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "comment_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_comment_generator")
    @Column(updatable = false, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    @Transient
    @Deprecated
    public AuthorInfo getAuthorInfo() {
        authorInfo.setAvatar(this.user != null ? this.user.getAvatar() : authorInfo.getAvatar());
        authorInfo.setFirstName(this.user != null ? this.user.getFirstName() : "Anonymous");
        authorInfo.setLastName(this.user != null ? this.user.getLastName() : "");
        return authorInfo;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_to")
    public BookComments getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(BookComments relatedTo) {
        this.relatedTo = relatedTo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
