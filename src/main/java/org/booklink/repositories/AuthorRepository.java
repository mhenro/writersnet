package org.booklink.repositories;

import org.booklink.models.entities.User;
import org.booklink.models.response.*;
import org.booklink.models.top_models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

/**
 * Created by mhenr on 16.10.2017.
 */
public interface AuthorRepository extends PagingAndSortingRepository<User, String> {
    @Query("SELECT new org.booklink.models.response.AuthorResponse(u.username, u.email, u.birthday, u.city, u.firstName, u.lastName, u.avatar, u.section, u.language, u.preferredLanguages, u.views, u.totalRating, u.totalVotes, u.online) FROM User u WHERE u.username = ?1")
    AuthorResponse findAuthor(final String username);

    @Query("SELECT new org.booklink.models.response.AuthorShortInfoResponse(u.username, u.firstName, u.lastName, u.avatar, u.preferredLanguages, u.views, u.totalRating, u.totalVotes, u.online) FROM User u WHERE UPPER(u.firstName) LIKE CONCAT(UPPER(?1), '%') AND u.enabled = true")  //TODO: sort by premium account
    Page<AuthorShortInfoResponse> findAuthorsByName(String name, Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.online = false WHERE u.username IN (SELECT s.author.username FROM Session s WHERE s.expired < ?1)")
    void setOfflineStatus(final Date currentDate);

    @Query("SELECT new org.booklink.models.response.AuthorShortInfoResponse(u.username, u.firstName, u.lastName, u.avatar, u.preferredLanguages, u.views, u.totalRating, u.totalVotes, u.online) FROM User u WHERE u.enabled = true")
    Page<AuthorShortInfoResponse> findAllEnabled(final Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorRating(u.username, u.firstName, u.lastName, COALESCE(sum(r.ratingId.estimation), 0), count(r.ratingId.estimation)) FROM User u LEFT JOIN u.books b LEFT JOIN b.rating r WHERE u.enabled = true GROUP BY u.username ORDER BY count(r.ratingId.estimation)*COALESCE(sum(r.ratingId.estimation), 0) DESC")
    Page<TopAuthorRating> findAllByRating(final Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorBookCount(u.username, u.firstName, u.lastName, count(b.name)) FROM User u LEFT JOIN u.books b WHERE u.enabled = true GROUP BY u.username ORDER BY count(b.name) DESC")
    Page<TopAuthorBookCount> findAllByBookCount(final Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorComments(u.username, u.firstName, u.lastName, u.commentsCount) FROM User u WHERE u.enabled = true ORDER BY u.commentsCount DESC")
    Page<TopAuthorComments> findAllByComments(final Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorViews(u.username, u.firstName, u.lastName, u.views) FROM User u WHERE u.enabled = true ORDER BY u.views DESC")
    Page<TopAuthorViews> findAllByViews(final Pageable pageable);

    @Query("SELECT new org.booklink.models.response.ChatGroupResponse(g) FROM User u LEFT JOIN u.chatGroups g WHERE u.username = ?1 AND u.enabled = true")
    Page<ChatGroupResponse> getChatGroups(final String userId, final Pageable pageable);

    //TODO: fixme!
    //@Query("SELECT new org.booklink.models.response.FriendResponse(u.username, u.firstName, u.lastName) FROM User u LEFT JOIN u.subscribers scr LEFT JOIN u.subscriptions sub WHERE scr.friendshipPK.subscription.username = ?1 AND sub.friendshipPK.subscriber.username = ?1 AND UPPER(u.firstName) LIKE UPPER(?2)||'%' AND u.enabled = true")
    //Page<FriendResponse> getFriends(final String userId, final String matcher, final Pageable pageable);
}