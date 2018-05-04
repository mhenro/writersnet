package com.writersnets.models.entities;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by mhenr on 30.12.2017.
 */
@Embeddable
public class SubscriptionPK implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private User subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;


    public User getSubscription() {
        return subscription;
    }
    public void setSubscription(User subscription) {
        this.subscription = subscription;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubscriptionPK that = (SubscriptionPK) o;

        if (subscription != null ? !subscription.equals(that.subscription) : that.subscription != null) return false;
        return owner != null ? owner.equals(that.owner) : that.owner == null;

    }

    @Override
    public int hashCode() {
        int result = subscription != null ? subscription.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
