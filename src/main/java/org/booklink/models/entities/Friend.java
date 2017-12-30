package org.booklink.models.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mhenr on 30.12.2017.
 */
@Entity
@Table(name = "friends")
public class Friend {
    private FriendPK friendPK;
    private Date added;

    @EmbeddedId
    public FriendPK getFriendPK() {
        return friendPK;
    }

    public void setFriendPK(FriendPK friendPK) {
        this.friendPK = friendPK;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }
}
