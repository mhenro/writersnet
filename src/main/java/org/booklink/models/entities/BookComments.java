package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.booklink.models.AuthorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mhenr on 06.11.2017.
 */
@Entity
@Table(name = "book_comments")
public class BookComments {
    private static final String defaultAvatar = "http://192.168.1.105:81/css/images/avatars/default_avatar.png";
    private Long id;
    private Book book;
    private User user;
    private String comment;
    private Long relatedTo;
    private Date created;

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
        AuthorInfo user = new AuthorInfo();
        user.setAvatar(this.user != null ? this.user.getAvatar() : defaultAvatar);
        user.setFirstName(this.user != null ? this.user.getFirstName() : "Anonymous");
        user.setLastName(this.user != null ? this.user.getLastName() : "");
        return user;
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
