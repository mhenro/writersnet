package org.booklink.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by mhenr on 30.12.2017.
 */
@Entity
@Table(name = "subscriptions")
public class Subscription {
    private SubscriptionPK subscriptionPK;
    private LocalDateTime added;

    @EmbeddedId
    public SubscriptionPK getSubscriptionPK() {
        return subscriptionPK;
    }

    public void setSubscriptionPK(SubscriptionPK subscriptionPK) {
        this.subscriptionPK = subscriptionPK;
    }

    public LocalDateTime getAdded() {
        return added;
    }

    public void setAdded(LocalDateTime added) {
        this.added = added;
    }
}
