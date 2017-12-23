package org.booklink.repositories;

import org.booklink.models.entities.Book;
import org.booklink.models.response.BookResponse;
import org.booklink.models.response.BookWithTextResponse;
import org.booklink.models.top_models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 02.10.2017.
 */
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    @Query("SELECT new org.booklink.models.response.BookResponse(b.id, b.bookSerie.id, b.bookSerie.name, b.commentsCount, b.cover, b.created, b.description, b.genre, b.language, b.lastUpdate, b.name, LENGTH(b.bookText.text), b.totalRating, b.totalVotes, b.views) FROM Book b LEFT JOIN b.bookSerie WHERE b.author.username = ?1 ORDER BY b.name")
    Page<BookResponse> findBooksByAuthor(final String authorId, final Pageable pageable);

    @Query("SELECT new org.booklink.models.response.BookWithTextResponse(b.id, b.bookSerie.id, b.bookSerie.name, b.author.username, b.author.firstName, b.author.lastName, b.commentsCount, b.cover, b.created, b.description, b.genre, b.language, b.lastUpdate, b.name, LENGTH(b.bookText.text), b.totalRating, b.totalVotes, b.views, b.bookText.id, b.bookText.text) FROM Book b LEFT JOIN b.bookSerie WHERE b.id = ?1 ORDER BY b.name")
    BookWithTextResponse getBookById(final Long id);

    @Query("SELECT b FROM Book b WHERE UPPER(b.name) LIKE CONCAT(UPPER(?1), '%') ORDER BY b.lastUpdate DESC")
    Page<Book> findBooksByName(String name, Pageable pageable);

    Page<Book> findAllByOrderByLastUpdateDesc(Pageable pageable);

    /* tops */
    @Query("SELECT new org.booklink.models.top_models.TopBookNovelties(b.id, b.name, b.lastUpdate) FROM Book b ORDER BY b.lastUpdate DESC")
    Page<TopBookNovelties> findAllByLastUpdate(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookRating(b.id, b.name, COALESCE(sum(r.ratingId.estimation), 0), count(r.ratingId.estimation)) FROM Book b LEFT JOIN b.rating r GROUP BY b.id ORDER BY count(r.ratingId.estimation)*COALESCE(sum(r.ratingId.estimation), 0) DESC")
    Page<TopBookRating> findAllByRating(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookVolume(b.id, b.name, COALESCE(length(t.text), 0) ) FROM Book b LEFT JOIN b.bookText t ORDER BY length(t.text) DESC")
    Page<TopBookVolume> findAllByVolume(Pageable pageable);

    //TODO: fixme
    //@Query("SELECT new org.booklink.models.top_models.TopBookComments(b.id, b.name, count(c.comment)) FROM Book b LEFT JOIN b.comments c GROUP BY b.id ORDER BY count(c.comment) DESC")
    //Page<TopBookComments> findAllByComments(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookViews(b.id, b.name, b.views) FROM Book b ORDER BY b.views DESC")
    Page<TopBookViews> findAllByViews(Pageable pageable);
}
