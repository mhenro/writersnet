package org.booklink.repositories;

import org.booklink.models.entities.BookComments;
import org.booklink.models.response.BookCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 06.11.2017.
 */
public interface BookCommentsRepository extends PagingAndSortingRepository<BookComments, Long> {
    @Query("SELECT new org.booklink.models.response.BookCommentResponse(c.id, c.book.id, c.user.username, c.user.firstName, c.user.lastName, c.user.avatar, c.comment, c.relatedTo, c.created) FROM BookComments c LEFT JOIN c.relatedTo LEFT JOIN c.user WHERE c.book.id = ?1 ORDER BY c.created DESC")
    Page<BookCommentResponse> findAllByBookId(Long bookId, Pageable pageable);

    Iterable<BookComments> findAllByBookId(Long bookId);
}
