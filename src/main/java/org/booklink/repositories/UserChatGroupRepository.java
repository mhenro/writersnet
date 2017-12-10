package org.booklink.repositories;

import org.booklink.models.entities.UserChatGroup;
import org.booklink.models.entities.UserChatGroupPK;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 10.12.2017.
 */
public interface UserChatGroupRepository extends CrudRepository<UserChatGroup, UserChatGroupPK> {
}
