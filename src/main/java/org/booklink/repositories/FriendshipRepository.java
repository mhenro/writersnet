package org.booklink.repositories;

import org.booklink.models.entities.Friendship;
import org.booklink.models.entities.FriendshipPK;
import org.booklink.models.response.FriendshipResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 05.12.2017.
 */
public interface FriendshipRepository extends PagingAndSortingRepository<Friendship, FriendshipPK> {
    @Query("SELECT new org.booklink.models.response.FriendshipResponse(f.date, f.active, f.friendshipPK.subscriber.username, " +
            "f.friendshipPK.subscriber.firstName, f.friendshipPK.subscriber.lastName, f.friendshipPK.subscriber.section.name, " +
            "f.friendshipPK.subscriber.avatar) FROM Friendship f LEFT JOIN f.friendshipPK.subscriber.subscriptions subs " +
            "WHERE f.friendshipPK.subscription.username = ?1 AND subs.friendshipPK.subscriber.username = ?1 " +
            "ORDER BY f.friendshipPK.subscriber.firstName")
    Page<FriendshipResponse> getAllFriends(final String userId, final Pageable pageable);

    @Query("SELECT new org.booklink.models.response.FriendshipResponse(f.date, f.active, f.friendshipPK.subscriber.username, " +
            "f.friendshipPK.subscriber.firstName, f.friendshipPK.subscriber.lastName, f.friendshipPK.subscriber.section.name, " +
            "f.friendshipPK.subscriber.avatar) FROM Friendship f LEFT JOIN f.friendshipPK.subscriber.subscriptions subs " +
            "WHERE f.friendshipPK.subscription.username = ?1 AND (subs.friendshipPK.subscriber.username != ?1 OR subs.friendshipPK.subscriber = NULL) " +
            "ORDER BY f.friendshipPK.subscriber.firstName")
    Page<FriendshipResponse> getAllSubscribers(final String userId, final Pageable pageable);

    @Query("SELECT new org.booklink.models.response.FriendshipResponse(f.date, f.active, f.friendshipPK.subscription.username, " +
            "f.friendshipPK.subscription.firstName, f.friendshipPK.subscription.lastName, f.friendshipPK.subscription.section.name, " +
            "f.friendshipPK.subscription.avatar) FROM Friendship f LEFT JOIN f.friendshipPK.subscription.subscribers subs " +
            "WHERE f.friendshipPK.subscriber.username = ?1 AND (subs.friendshipPK.subscription.username != ?1 OR subs.friendshipPK.subscription = NULL) " +
            "ORDER BY f.friendshipPK.subscriber.firstName")
    Page<FriendshipResponse> getAllSubscriptions(final String userId, final Pageable pageable);
}
