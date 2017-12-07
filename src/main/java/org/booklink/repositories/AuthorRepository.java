package org.booklink.repositories;

import org.booklink.models.entities.User;
import org.booklink.models.response_models.ChatGroupResponse;
import org.booklink.models.response_models.MessageResponse;
import org.booklink.models.top_models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by mhenr on 16.10.2017.
 */
public interface AuthorRepository extends PagingAndSortingRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.enabled = true")
    Page<User> findAllEnabled(final Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorRating(u.username, u.firstName, u.lastName, COALESCE(sum(r.ratingId.estimation), 0), count(r.ratingId.estimation)) FROM User u LEFT JOIN u.books b LEFT JOIN b.rating r GROUP BY u.username ORDER BY count(r.ratingId.estimation)*COALESCE(sum(r.ratingId.estimation), 0) DESC")
    Page<TopAuthorRating> findAllByRating(final Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorBookCount(u.username, u.firstName, u.lastName, count(b.name)) FROM User u LEFT JOIN u.books b GROUP BY u.username ORDER BY count(b.name) DESC")
    Page<TopAuthorBookCount> findAllByBookCount(final Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorComments(u.username, u.firstName, u.lastName, count(c.comment)) FROM User u LEFT JOIN u.books b LEFT JOIN b.comments c GROUP BY u.username ORDER BY count(c.comment) DESC")
    Page<TopAuthorComments> findAllByComments(final Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorViews(u.username, u.firstName, u.lastName, u.views) FROM User u ORDER BY u.views DESC")
    Page<TopAuthorViews> findAllByViews(final Pageable pageable);

    @Query("SELECT new org.booklink.models.response_models.ChatGroupResponse(g.userChatGroupPK.group.id, g.userChatGroupPK.group.creator, g.userChatGroupPK.group.created, g.userChatGroupPK.group.lastMessage, g) FROM User u LEFT JOIN u.chatGroups g WHERE g.userChatGroupPK.user.username = ?1")
    Page<ChatGroupResponse> getChatGroups(final String userId, final Pageable pageable);
}
