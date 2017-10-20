package org.booklink.models.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by mhenr on 18.10.2017.
 */
@Embeddable
public class RatingId implements Serializable {
    private Long bookId;
    private Integer value;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
