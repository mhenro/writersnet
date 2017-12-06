package org.booklink.models.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mhenr on 06.12.2017.
 */
@Entity
@Table(name = "chat_groups_users")
public class UserChatGroup {
    private UserChatGroupPK userChatGroupPK;

    @EmbeddedId
    public UserChatGroupPK getUserChatGroupPK() {
        return userChatGroupPK;
    }

    public void setUserChatGroupPK(UserChatGroupPK userChatGroupPK) {
        this.userChatGroupPK = userChatGroupPK;
    }
}
