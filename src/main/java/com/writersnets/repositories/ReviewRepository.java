package com.writersnets.repositories;

import com.writersnets.models.entities.books.Review;
import com.writersnets.models.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 04.01.2018.
 */
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {
    @Query("SELECT new com.writersnets.models.response.ReviewResponse(r.id, r.book.id, r.book.name, r.book.cover, r.name, r.author.username, r.author.firstName, r.author.lastName, r.score, r.text, r.likes, r.dislikes) FROM Review r WHERE r.book.id = ?1 ORDER BY r.dislikes, r.likes DESC")
    Page<ReviewResponse> getReviewsByBookId(final Long bookId, final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.ReviewResponse(r.id, r.book.id, r.book.name, r.book.cover, r.name, r.author.username, r.author.firstName, r.author.lastName, r.score, r.text, r.likes, r.dislikes) FROM Review r ORDER BY r.dislikes, r.likes DESC")
    Page<ReviewResponse> getReviews(final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.ReviewResponse(r.id, r.book.id, r.book.name, r.book.cover, r.name, r.author.username, r.author.firstName, r.author.lastName, r.score, r.text, r.likes, r.dislikes) FROM Review r WHERE r.id = ?1")
    ReviewResponse getReviewDetails(final Long reviewId);
}
