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

    //select username, count(name) from users LEFT JOIN book ON username = author GROUP BY username ORDER BY count(name) DESC;
    @Query("SELECT new org.booklink.models.top_models.TopAuthorBookCount(u.username, count(b.name)) FROM User u LEFT JOIN u.books b GROUP BY u.username ORDER BY count(b.name) DESC")
    Page<TopAuthorBookCount> findAllByBookCount(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookRating(b.id, b.name, COALESCE(sum(r.ratingId.estimation), 0), count(r.ratingId.estimation)) FROM Book b LEFT JOIN b.rating r GROUP BY b.id ORDER BY count(r.ratingId.estimation) DESC")
    Page<TopAuthorRating> findAllByRating(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookVolume(b.id, b.name, COALESCE(length(t.text), 0) ) FROM Book b LEFT JOIN b.bookText t ORDER BY length(t.text) DESC")
    Page<TopBookVolume> findAllByVolume(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookComments(b.id, b.name, count(c.comment)) FROM Book b LEFT JOIN b.comments c GROUP BY b.id ORDER BY count(c.comment) DESC")
    Page<TopBookComments> findAllByComments(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookViews(b.id, b.name, b.views) FROM Book b ORDER BY b.views DESC")
    Page<TopBookViews> findAllByViews(Pageable pageable);
}
