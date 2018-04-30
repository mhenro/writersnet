package com.writersnets.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by mhenr on 30.12.2017.
 */
@Entity
@Table(name = "friends")
public class Friend {
    private FriendPK friendPK;
    private LocalDateTime added;

    @EmbeddedId
    public FriendPK getFriendPK() {
        return friendPK;
    }

    public void setFriendPK(FriendPK friendPK) {
        this.friendPK = friendPK;
    }

    public LocalDateTime getAdded() {
        return added;
    }

    public void setAdded(LocalDateTime added) {
        this.added = added;
    }
}
