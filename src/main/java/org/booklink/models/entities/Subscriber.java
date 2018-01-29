package org.booklink.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by mhenr on 30.12.2017.
 */
@Entity
@Table(name = "subscribers")
public class Subscriber {
    private SubscriberPK subscriberPK;
    private LocalDateTime added;

    @EmbeddedId
    public SubscriberPK getSubscriberPK() {
        return subscriberPK;
    }

    public void setSubscriberPK(SubscriberPK subscriberPK) {
        this.subscriberPK = subscriberPK;
    }

    public LocalDateTime getAdded() {
        return added;
    }

    public void setAdded(LocalDateTime added) {
        this.added = added;
    }
}
