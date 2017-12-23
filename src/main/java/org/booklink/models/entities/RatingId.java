package org.booklink.models.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mhenr on 18.10.2017.
 */
@Embeddable
public class RatingId implements Serializable {
    private Book book;
    private Integer estimation;
    private String clientIp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getEstimation() {
        return estimation;
    }

    public void setEstimation(Integer estimation) {
        this.estimation = estimation;
    }

    @Column(name = "client_ip")
    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatingId ratingId = (RatingId) o;

        if (book != null ? !book.equals(ratingId.book) : ratingId.book != null) return false;
        if (estimation != null ? !estimation.equals(ratingId.estimation) : ratingId.estimation != null) return false;
        return clientIp != null ? clientIp.equals(ratingId.clientIp) : ratingId.clientIp == null;

    }

    @Override
    public int hashCode() {
        int result = book != null ? book.hashCode() : 0;
        result = 31 * result + (estimation != null ? estimation.hashCode() : 0);
        result = 31 * result + (clientIp != null ? clientIp.hashCode() : 0);
        return result;
    }
}
