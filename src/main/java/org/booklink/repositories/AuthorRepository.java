package org.booklink.repositories;

import org.booklink.models.entities.User;
import org.booklink.models.top_models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 16.10.2017.
 */
public interface AuthorRepository extends PagingAndSortingRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.enabled = true")
    Page<User> findAllEnabled(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorRating(u.username, u.firstName, u.lastName, COALESCE(sum(r.ratingId.estimation), 0), count(r.ratingId.estimation)) FROM User u LEFT JOIN u.books b LEFT JOIN b.rating r GROUP BY u.username ORDER BY count(r.ratingId.estimation)*COALESCE(sum(r.ratingId.estimation), 0) DESC")
    Page<TopAuthorRating> findAllByRating(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorBookCount(u.username, u.firstName, u.lastName, count(b.name)) FROM User u LEFT JOIN u.books b GROUP BY u.username ORDER BY count(b.name) DESC")
    Page<TopAuthorBookCount> findAllByBookCount(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorComments(u.username, u.firstName, u.lastName, count(c.comment)) FROM User u LEFT JOIN u.books b LEFT JOIN b.comments c GROUP BY u.username ORDER BY count(c.comment) DESC")
    Page<TopAuthorComments> findAllByComments(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopAuthorViews(u.username, u.firstName, u.lastName, u.views) FROM User u ORDER BY u.views DESC")
    Page<TopAuthorViews> findAllByViews(Pageable pageable);
}
