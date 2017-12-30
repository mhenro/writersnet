package org.booklink.models.entities;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by mhenr on 30.12.2017.
 */
@Embeddable
public class FriendPK implements Serializable{
    private User friend;
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
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

        FriendPK friendPK = (FriendPK) o;

        if (friend != null ? !friend.equals(friendPK.friend) : friendPK.friend != null) return false;
        return owner != null ? owner.equals(friendPK.owner) : friendPK.owner == null;

    }

    @Override
    public int hashCode() {
        int result = friend != null ? friend.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
