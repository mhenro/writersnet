package org.booklink.models.response_models;

import org.booklink.models.entities.Friendship;
import org.booklink.models.entities.User;

import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
public class FriendshipResponse {
    private boolean active;
    private Date date;
    private String subscriberAvatar;
    private String subscriberFullName;
    private String subscriberId;
    private String subscriberSectionName;
    private String subscriptionAvatar;
    private String subscriptionFullName;
    private String subscriptionId;
    private String subscriptionSectionName;

    public FriendshipResponse(final Friendship friendship) {
        if (friendship == null) {
            return;
        }
        this.active = friendship.isActive();
        this.date = friendship.getDate();
        this.subscriberAvatar = friendship.getFriendshipPK().getSubscriber().getAvatar();
        this.subscriberFullName = friendship.getFriendshipPK().getSubscriber().getFullName();
        this.subscriberId = friendship.getFriendshipPK().getSubscriber().getUsername();
        this.subscriberSectionName = friendship.getFriendshipPK().getSubscriber().getSection().getName();
        this.subscriptionAvatar = friendship.getFriendshipPK().getSubscription().getAvatar();
        this.subscriptionFullName = friendship.getFriendshipPK().getSubscription().getFullName();
        this.subscriptionId = friendship.getFriendshipPK().getSubscription().getUsername();
        this.subscriptionSectionName = friendship.getFriendshipPK().getSubscription().getSection().getName();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubscriberAvatar() {
        return subscriberAvatar;
    }

    public void setSubscriberAvatar(String subscriberAvatar) {
        this.subscriberAvatar = subscriberAvatar;
    }

    public String getSubscriberFullName() {
        return subscriberFullName;
    }

    public void setSubscriberFullName(String subscriberFullName) {
        this.subscriberFullName = subscriberFullName;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriberSectionName() {
        return subscriberSectionName;
    }

    public void setSubscriberSectionName(String subscriberSectionName) {
        this.subscriberSectionName = subscriberSectionName;
    }

    public String getSubscriptionAvatar() {
        return subscriptionAvatar;
    }

    public void setSubscriptionAvatar(String subscriptionAvatar) {
        this.subscriptionAvatar = subscriptionAvatar;
    }

    public String getSubscriptionFullName() {
        return subscriptionFullName;
    }

    public void setSubscriptionFullName(String subscriptionFullName) {
        this.subscriptionFullName = subscriptionFullName;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionSectionName() {
        return subscriptionSectionName;
    }

    public void setSubscriptionSectionName(String subscriptionSectionName) {
        this.subscriptionSectionName = subscriptionSectionName;
    }
}
