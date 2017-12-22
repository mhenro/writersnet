package org.booklink.repositories;

import org.booklink.models.entities.BookSerie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by mhenr on 29.10.2017.
 */
public interface SerieRepository extends PagingAndSortingRepository<BookSerie, Long> {
    @Query("SELECT s FROM BookSerie s WHERE s.author.username = ?1")
    Page<BookSerie> findAllByUserId(final String userId, Pageable pageable);
}
