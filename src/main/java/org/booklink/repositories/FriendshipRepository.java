package org.booklink.repositories;

import org.booklink.models.entities.Friendship;
import org.booklink.models.entities.FriendshipPK;
import org.booklink.models.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 05.12.2017.
 */
public interface FriendshipRepository extends CrudRepository<Friendship, FriendshipPK> {
}
