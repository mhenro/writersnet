package com.writersnets.models.entities.groups;

import com.writersnets.models.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by mhenr on 30.12.2017.
 */
@Embeddable
@Audited
@Getter @Setter @NoArgsConstructor
public class FriendPK implements Serializable{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private User friend;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

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
