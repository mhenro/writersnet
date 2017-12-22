package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mhenr on 05.12.2017.
 */
@Embeddable
public class FriendshipPK implements Serializable{
    private User subscriber;
    private User subscription;

    @ManyToOne
    @JoinColumn(name = "subscriber_id", referencedColumnName = "username")
    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }

    @ManyToOne
    @JoinColumn(name = "subscription_id", referencedColumnName = "username")
    public User getSubscription() {
        return subscription;
    }

    public void setSubscription(User subscription) {
        this.subscription = subscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendshipPK that = (FriendshipPK) o;

        if (subscriber != null ? !subscriber.equals(that.subscriber) : that.subscriber != null) return false;
        return subscription != null ? subscription.equals(that.subscription) : that.subscription == null;

    }

    @Override
    public int hashCode() {
        int result = subscriber != null ? subscriber.hashCode() : 0;
        result = 31 * result + (subscription != null ? subscription.hashCode() : 0);
        return result;
    }
}
