package com.writersnets.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 30.12.2017.
 */
@Entity
@Table(name = "friends")
public class Friend extends AbstractEntity {
    @EmbeddedId
    private FriendPK friendPK;
    private LocalDateTime added;


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
