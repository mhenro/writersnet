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
    private Long relatedTo;
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

    @OneToOne
    @JoinColumn(name = "book_id")
    @JsonIgnore
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public User getUser() {
        return user;
    }

    @Transient
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

    @Column(name = "related_to")
    public Long getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(Long relatedTo) {
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
