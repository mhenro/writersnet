package org.booklink.repositories;

import org.booklink.models.entities.News;
import org.booklink.models.response_models.NewsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 13.12.2017.
 */
public interface NewsRepository extends PagingAndSortingRepository<News, Long> {
    @Query("SELECT new org.booklink.models.response_models.NewsResponse(n.id, n.type, n.author, n.book, n.created) FROM News n LEFT JOIN n.author.subscriptions s WHERE s.friendshipPK.subscriber.username = ?1 ORDER BY n.created DESC")
    Page<NewsResponse> findAllNews(final String authorId, final Pageable pageable);
}