package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by mhenr on 04.12.2017.
 */
@Entity
@Table(name = "friendships")
public class Friendship implements Serializable {
    private User subscriber;
    private User subscription;
    private Date date;
    private boolean active;

    @Id
    @ManyToOne
    @JoinColumn(name = "subscription_id", referencedColumnName = "username")
    @JsonIgnore
    public User getSubscriber() {
        return subscriber;
    }

    @Transient
    public String getSubscriberName() {
        return subscription.getUsername();
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "subscriber_id", referencedColumnName = "username")
    @JsonIgnore
    public User getSubscription() {
        return subscription;
    }

    @Transient
    public String getSubscriptionName() {
        return subscriber.getUsername();
    }

    public void setSubscription(User subscription) {
        this.subscription = subscription;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
