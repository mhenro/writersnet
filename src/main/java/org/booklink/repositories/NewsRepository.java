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
    //TODO: news should be getted from subscriptions as well!
    @Query("SELECT new org.booklink.models.response.NewsResponse(n) FROM News n, Friend f " +
            "WHERE f.friendPK.owner.username = ?1 AND f.friendPK.friend = n.author ORDER BY n.created DESC")
    Page<NewsResponse> findAllNews(final String authorId, final Pageable pageable);
}