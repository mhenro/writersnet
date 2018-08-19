package com.writersnets.repositories;

import com.writersnets.models.entities.groups.Friend;
import com.writersnets.models.entities.groups.FriendPK;
import com.writersnets.models.response.FriendResponse;
import com.writersnets.models.response.FriendshipResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

/**
 * Created by mhenr on 05.12.2017.
 */
public interface FriendshipRepository extends PagingAndSortingRepository<Friend, FriendPK> {
    @Query("SELECT new com.writersnets.models.response.FriendshipResponse(tmp.added, true, f.username, f.firstName, f.lastName, f.section.name, f.avatar) FROM Friend tmp LEFT JOIN tmp.friendPK.friend f WHERE tmp.friendPK.owner.username = ?1")
    Page<FriendshipResponse> getAllFriends(final String userId, final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.FriendResponse(f.username, f.firstName, f.lastName) FROM Friend tmp LEFT JOIN tmp.friendPK.friend f WHERE tmp.friendPK.owner.username = ?1 AND UPPER(f.firstName) LIKE UPPER(?2)||'%' AND f.enabled = true")
    Page<FriendResponse> getAllFriendsByName(final String userId, final String matcher, final Pageable pageable);

    @Query("SELECT COUNT(s) FROM Subscriber s WHERE s.subscriberPK.owner.username = ?1")
    Long getNewFriendsCount(final String userId);

    @Query("SELECT new com.writersnets.models.response.FriendshipResponse(tmp.added, true, f.username, f.firstName, f.lastName, f.section.name, f.avatar) FROM Subscriber tmp LEFT JOIN tmp.subscriberPK.subscriber f WHERE tmp.subscriberPK.owner.username = ?1")
    Page<FriendshipResponse> getAllSubscribers(final String userId, final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.FriendshipResponse(tmp.added, true, f.username, f.firstName, f.lastName, f.section.name, f.avatar) FROM Subscription tmp LEFT JOIN tmp.subscriptionPK.subscription f WHERE tmp.subscriptionPK.owner.username = ?1")
    Page<FriendshipResponse> getAllSubscriptions(final String userId, final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.FriendshipResponse(tmp.added, true, f.username, f.firstName, f.lastName, f.section.name, f.avatar) FROM Friend tmp LEFT JOIN tmp.friendPK.friend f WHERE tmp.friendPK.owner.username = ?1 AND tmp.friendPK.friend.username = ?2")
    FriendshipResponse getFriendByName(final String owner, final String friend);

    @Query("SELECT new com.writersnets.models.response.FriendshipResponse(tmp.added, true, f.username, f.firstName, f.lastName, f.section.name, f.avatar) FROM Subscriber tmp LEFT JOIN tmp.subscriberPK.subscriber f WHERE tmp.subscriberPK.owner.username = ?1 AND tmp.subscriberPK.subscriber.username = ?2")
    FriendshipResponse getSubscriberByName(final String owner, final String subscriber);

    @Query("SELECT new com.writersnets.models.response.FriendshipResponse(tmp.added, true, f.username, f.firstName, f.lastName, f.section.name, f.avatar) FROM Subscription tmp LEFT JOIN tmp.subscriptionPK.subscription f WHERE tmp.subscriptionPK.owner.username = ?1 AND tmp.subscriptionPK.subscription.username = ?2")
    FriendshipResponse getSubscriptionByName(final String owner, final String subscription);

    @Modifying
    @Query("DELETE FROM Friend f WHERE f.friendPK.owner.username = ?1 AND f.friendPK.friend.username = ?2")
    void removeFriend(final String owner, final String friend);

    @Modifying
    @Query("DELETE FROM Subscriber f WHERE f.subscriberPK.owner.username = ?1 AND f.subscriberPK.subscriber.username = ?2")
    void removeSubscriber(final String owner, final String subscriber);

    @Modifying
    @Query("DELETE FROM Subscription f WHERE f.subscriptionPK.owner.username = ?1 AND f.subscriptionPK.subscription.username = ?2")
    void removeSubscription(final String owner, final String subscription);

    @Modifying
    @Query(value = "INSERT INTO friends(friend_id, owner_id, added, opt_lock) VALUES(?2, ?1, ?3, 0)", nativeQuery = true)
    void addToFriends(final String owner, final String friend, final Date added);

    @Modifying
    @Query(value = "INSERT INTO subscribers(subscriber_id, owner_id, added, opt_lock) VALUES(?2, ?1, ?3, 0)", nativeQuery = true)
    void addToSubscribers(final String owner, final String subscriber, final Date added);

    @Modifying
    @Query(value = "INSERT INTO subscriptions(subscription_id, owner_id, added, opt_lock) VALUES(?2, ?1, ?3, 0)", nativeQuery = true)
    void addToSubscriptions(final String owner, final String subscription, final Date added);
}
