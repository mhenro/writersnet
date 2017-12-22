package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.booklink.models.request_models.AuthorInfo;

import javax.persistence.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        if (book != null) {
            book.getComments().add(this);
        }
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
