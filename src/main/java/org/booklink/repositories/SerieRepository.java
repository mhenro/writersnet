package org.booklink.repositories;

import org.booklink.models.entities.BookSerie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 29.10.2017.
 */
public interface SerieRepository extends PagingAndSortingRepository<BookSerie, Long> {
    Page<BookSerie> findAllByUserId(final String userId, Pageable pageable);
}
