package org.booklink.repositories;

import org.booklink.models.entities.Comment;
import org.booklink.models.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 06.11.2017.
 */
public interface BookCommentsRepository extends PagingAndSortingRepository<Comment, Long> {
    @Query("SELECT new org.booklink.models.response.CommentResponse(c.id, c.book.id, c.user.username, c.user.firstName, c.user.lastName, c.user.avatar, c.comment, c.relatedTo, c.created) FROM Comment c LEFT JOIN c.relatedTo LEFT JOIN c.user WHERE c.book.id = ?1 ORDER BY c.created DESC")
    Page<CommentResponse> findAllByBookId(Long bookId, Pageable pageable);
}
