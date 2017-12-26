package org.booklink.models.response;

import org.booklink.models.entities.Friendship;

import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
public class FriendshipResponse {
    private Date date;
    private Boolean active;
    private String id;
    private String name;
    private String  section;
    private String avatar;

    public FriendshipResponse(final Date date, final Boolean active, final String id, final String firstName, final String lastName,
                              final String section, final String avatar) {
        this.date = date;
        this.active = active;
        this.id = id;
        this.name = firstName + " " + lastName;
        this.section = section;
        this.avatar = avatar;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /*
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
    */
}
