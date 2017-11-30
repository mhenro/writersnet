package org.booklink.repositories;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.User;
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
}
