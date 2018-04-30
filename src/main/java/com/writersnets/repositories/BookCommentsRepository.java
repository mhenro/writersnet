package com.writersnets.repositories;

import com.writersnets.models.entities.Comment;
import com.writersnets.models.response.CommentResponse;
import com.writersnets.models.response.DetailedCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

/**
 * Created by mhenr on 06.11.2017.
 */
public interface BookCommentsRepository extends PagingAndSortingRepository<Comment, Long> {
    @Query("SELECT new com.writersnets.models.response.CommentResponse(c.id, c.book.id, c.user.username, c.user.firstName, c.user.lastName, c.user.avatar, c.comment, c.relatedTo, c.created) FROM Comment c LEFT JOIN c.relatedTo LEFT JOIN c.user WHERE c.book.id = ?1 ORDER BY c.created DESC")
    Page<CommentResponse> findAllByBookId(Long bookId, Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.DetailedCommentResponse(c.id, c.book.id, c.book.name, c.book.cover, c.book.author.username, c.book.author.firstName, c.book.author.lastName, c.book.author.avatar, c.user.firstName, c.user.lastName, c.user.avatar, c.comment, c.created) FROM Comment c LEFT JOIN c.user WHERE c.created IN (SELECT MAX(sub.created) FROM Comment sub GROUP BY sub.book.id) ORDER BY c.created DESC")
    Page<DetailedCommentResponse> getCommentsGroupByBookOrderByDate(final Pageable pageable);
}
