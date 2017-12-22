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
    private FriendshipPK friendshipPK;
    private Date date;
    private Boolean active;

    @EmbeddedId
    public FriendshipPK getFriendshipPK() {
        return friendshipPK;
    }

    public void setFriendshipPK(FriendshipPK friendshipPK) {
        this.friendshipPK = friendshipPK;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(nullable = false)
    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    /* ----------------------------------------business logic--------------------------------------------------- */

    @Transient
    public String getSubscriberId() {
        return getFriendshipPK().getSubscriber().getUsername();
    }

    @Transient
    public String getSubscriberFullName() {
        return getFriendshipPK().getSubscriber().getFullName();
    }

    @Transient
    public String getSubscriberSectionName() {
        return getFriendshipPK().getSubscriber().getSection().getName();
    }

    @Transient
    public String getSubscriberAvatar() {
        return getFriendshipPK().getSubscriber().getAvatar();
    }

    @Transient
    public String getSubscriptionId() {
        return getFriendshipPK().getSubscription().getUsername();
    }

    @Transient
    public String getSubscriptionFullName() {
        return getFriendshipPK().getSubscription().getFullName();
    }

    @Transient
    public String getSubscriptionSectionName() {
        return getFriendshipPK().getSubscription().getSection().getName();
    }

    @Transient
    public String getSubscriptionAvatar() {
        return getFriendshipPK().getSubscription().getAvatar();
    }
}
