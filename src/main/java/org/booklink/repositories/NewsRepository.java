package org.booklink.repositories;

import org.booklink.models.entities.News;
import org.booklink.models.response.NewsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 13.12.2017.
 */
public interface NewsRepository extends PagingAndSortingRepository<News, Long> {
    /*SELECT id, author_id from news WHERE author_id IN (SELECT subscription_id FROM subscriptions WHERE owner_id = 'nebula')
		OR author_id IN (SELECT friend_id FROM friends WHERE owner_id = 'nebula');*/
    @Query("SELECT new org.booklink.models.response.NewsResponse(n) FROM News n " +
            "WHERE n.author.username IN (SELECT s.subscriptionPK.subscription.username FROM Subscription s WHERE s.subscriptionPK.owner.username = ?1)" +
            "OR n.author.username IN (SELECT f.friendPK.friend.username FROM Friend f WHERE f.friendPK.owner.username = ?1) ORDER BY n.created DESC")
    Page<NewsResponse> findAllNews(final String authorId, final Pageable pageable);
}