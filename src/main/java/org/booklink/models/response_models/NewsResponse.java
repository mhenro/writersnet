package org.booklink.models.response_models;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.News;
import org.booklink.models.entities.User;

import java.util.Date;
import java.util.Optional;

/**
 * Created by mhenr on 13.12.2017.
 */
public class NewsResponse {
    private Long id;
    private Long type;
    private String authorId;
    private String authorFullName;
    private String authorAvatar;
    private Long bookId;
    private String bookName;
    private Date created;
    private String subscriptionId;
    private String subscriptionFullName;

    public NewsResponse(final News news) {
        this.id = news.getId();
        this.type = news.getType();
        this.authorId = news.getAuthor().getUsername();
        this.authorFullName = news.getAuthor().getFullName();
        this.authorAvatar = news.getAuthor().getAvatar();
        this.bookId = Optional.ofNullable(news.getBook()).map(Book::getId).orElse(null);
        this.bookName = Optional.ofNullable(news.getBook()).map(Book::getName).orElse(null);
        this.created = news.getCreated();
        this.subscriptionId = Optional.ofNullable(news.getSubscription()).map(User::getUsername).orElse(null);
        this.subscriptionFullName = Optional.ofNullable(news.getSubscription()).map(User::getFullName).orElse(null);
    }

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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionFullName() {
        return subscriptionFullName;
    }

    public void setSubscriptionFullName(String subscriptionFullName) {
        this.subscriptionFullName = subscriptionFullName;
    }
}
