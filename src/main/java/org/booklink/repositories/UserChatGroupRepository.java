package org.booklink.repositories;

import org.booklink.models.entities.ChatGroup;
import org.booklink.models.entities.UserChatGroup;
import org.booklink.models.entities.UserChatGroupPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by mhenr on 10.12.2017.
 */
public interface UserChatGroupRepository extends CrudRepository<UserChatGroup, UserChatGroupPK> {
    @Query("SELECT ug.userChatGroupPK.group FROM UserChatGroup ug WHERE ug.userChatGroupPK.user.username = ?1")
    List<ChatGroup> findAllByUserId(final String userId);
}
