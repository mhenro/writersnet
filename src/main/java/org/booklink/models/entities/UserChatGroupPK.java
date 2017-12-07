package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mhenr on 06.12.2017.
 */
@Embeddable
public class UserChatGroupPK implements Serializable {
    private ChatGroup group;
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @JsonIgnore
    public ChatGroup getGroup() {
        return group;
    }

    public void setGroup(ChatGroup group) {
        this.group = group;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserChatGroupPK that = (UserChatGroupPK) o;

        if (!group.equals(that.group)) return false;
        return user.equals(that.user);

    }

    @Override
    public int hashCode() {
        int result = group.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}
