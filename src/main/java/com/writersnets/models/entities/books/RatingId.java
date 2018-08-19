package com.writersnets.models.entities.books;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mhenr on 18.10.2017.
 */
@Embeddable
@Audited
@Getter @Setter @NoArgsConstructor
public class RatingId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    private Integer estimation;

    @Column(name = "client_ip")
    private String clientIp;

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
