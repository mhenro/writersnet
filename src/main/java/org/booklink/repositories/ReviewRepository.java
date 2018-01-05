package org.booklink.repositories;

import org.booklink.models.entities.Review;
import org.booklink.models.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 04.01.2018.
 */
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {
    @Query("SELECT new org.booklink.models.response.ReviewResponse(r.id, r.book.id, r.book.name, r.book.cover, r.name, r.author.username, r.author.firstName, r.author.lastName, r.score, r.text) FROM Review r WHERE r.book.id = ?1")
    Page<ReviewResponse> getReviewsByBookId(final Long bookId, final Pageable pageable);

    @Query("SELECT new org.booklink.models.response.ReviewResponse(r.id, r.book.id, r.book.name, r.book.cover, r.name, r.author.username, r.author.firstName, r.author.lastName, r.score, r.text) FROM Review r ")
    Page<ReviewResponse> getReviews(final Pageable pageable);
}
