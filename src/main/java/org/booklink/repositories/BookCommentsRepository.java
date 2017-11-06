package org.booklink.repositories;

import org.booklink.models.entities.BookComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 06.11.2017.
 */
public interface BookCommentsRepository extends PagingAndSortingRepository<BookComments, Long> {
    Page<BookComments> findAllByBookId(Long bookId, Pageable pageable);
}
