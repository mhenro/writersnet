package org.booklink.repositories;

import org.booklink.models.entities.Book;
import org.booklink.models.top_models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 02.10.2017.
 */
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    @Query("SELECT new org.booklink.models.top_models.TopBookNovelties(b.id, b.name, b.lastUpdate) FROM Book b ORDER BY b.lastUpdate DESC")
    Page<TopBookNovelties> findAllByLastUpdate(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookRating(b.id, b.name, COALESCE(sum(r.ratingId.estimation), 0), count(r.ratingId.estimation)) FROM Book b LEFT JOIN b.rating r GROUP BY b.id ORDER BY count(r.ratingId.estimation)*COALESCE(sum(r.ratingId.estimation), 0) DESC")
    Page<TopBookRating> findAllByRating(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookVolume(b.id, b.name, COALESCE(length(t.text), 0) ) FROM Book b LEFT JOIN b.bookText t ORDER BY length(t.text) DESC")
    Page<TopBookVolume> findAllByVolume(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookComments(b.id, b.name, count(c.comment)) FROM Book b LEFT JOIN b.comments c GROUP BY b.id ORDER BY count(c.comment) DESC")
    Page<TopBookComments> findAllByComments(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookViews(b.id, b.name, b.views) FROM Book b ORDER BY b.views DESC")
    Page<TopBookViews> findAllByViews(Pageable pageable);
}
