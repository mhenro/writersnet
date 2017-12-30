package org.booklink.models.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mhenr on 30.12.2017.
 */
@Entity
@Table(name = "subscribers")
public class Subscriber {
    private SubscriberPK subscriberPK;
    private Date added;

    @EmbeddedId
    public SubscriberPK getSubscriberPK() {
        return subscriberPK;
    }

    public void setSubscriberPK(SubscriberPK subscriberPK) {
        this.subscriberPK = subscriberPK;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }
}
