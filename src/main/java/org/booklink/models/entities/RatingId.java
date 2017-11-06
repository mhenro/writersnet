package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mhenr on 18.10.2017.
 */
@Embeddable
public class RatingId implements Serializable {
    private Long bookId;
    private Integer estimation;
    private String clientIp;

    @JsonIgnore
    @Column(name = "book_id")
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
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

        if (bookId != null ? !bookId.equals(ratingId.bookId) : ratingId.bookId != null) return false;
        if (estimation != null ? !estimation.equals(ratingId.estimation) : ratingId.estimation != null) return false;
        return clientIp != null ? clientIp.equals(ratingId.clientIp) : ratingId.clientIp == null;

    }

    @Override
    public int hashCode() {
        int result = bookId != null ? bookId.hashCode() : 0;
        result = 31 * result + (estimation != null ? estimation.hashCode() : 0);
        result = 31 * result + (clientIp != null ? clientIp.hashCode() : 0);
        return result;
    }
}
