package com.writersnets.repositories;

import com.writersnets.models.entities.books.BookSerie;
import com.writersnets.models.response.BookSerieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 29.10.2017.
 */
public interface SerieRepository extends PagingAndSortingRepository<BookSerie, Long> {
    @Query("SELECT new com.writersnets.models.response.BookSerieResponse(s.id, s.name) FROM BookSerie s WHERE s.author.username = ?1 ORDER BY s.name ASC")
    Page<BookSerieResponse> findAllByUserId(final String userId, Pageable pageable);
}
