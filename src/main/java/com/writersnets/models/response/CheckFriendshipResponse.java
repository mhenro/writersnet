package com.writersnets.models.response;

/**
 * Created by mhenr on 23.12.2017.
 */
public class CheckFriendshipResponse {
    private boolean friend;
    private boolean subscriber;
    private boolean subscription;

    public CheckFriendshipResponse(final boolean friend, final boolean subscriber, final boolean subscription) {
        this.friend = friend;
        this.subscriber = subscriber;
        this.subscription = subscription;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public boolean isSubscriber() {
        return subscriber;
    }

    public void setSubscriber(boolean subscriber) {
        this.subscriber = subscriber;
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }
}
