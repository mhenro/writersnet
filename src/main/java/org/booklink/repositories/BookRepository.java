package org.booklink.repositories;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.User;
import org.booklink.models.request_models.BookRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by mhenr on 02.10.2017.
 */
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    Page<Book> findAllByOrderByLastUpdateAsc(Pageable pageable);
    @Query("SELECT new org.booklink.models.request_models.BookRequest(b.id, b.name, COALESCE(sum(r.ratingId.estimation), 0), count(r.ratingId.estimation)) FROM Book b LEFT JOIN b.rating r GROUP BY b.id ORDER BY count(r.ratingId.estimation)")
    Page<BookRequest> findAllByRating(Pageable pageable);

   // Page<Book> findAllByOrderBySize(Pageable pageable);
}
