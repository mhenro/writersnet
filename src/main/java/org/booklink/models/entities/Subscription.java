package org.booklink.models.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mhenr on 30.12.2017.
 */
@Entity
@Table(name = "subscriptions")
public class Subscription {
    private SubscriptionPK subscriptionPK;
    private Date added;

    @EmbeddedId
    public SubscriptionPK getSubscriptionPK() {
        return subscriptionPK;
    }

    public void setSubscriptionPK(SubscriptionPK subscriptionPK) {
        this.subscriptionPK = subscriptionPK;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }
}
