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
    @Query("SELECT new org.booklink.models.response.BookResponse(b.id, b.bookSerie.id, b.bookSerie.name, b.commentsCount, b.cover, b.created, b.description, b.genre, b.language, b.lastUpdate, b.name, LENGTH(b.bookText.text), b.totalRating, b.totalVotes, b.views, b.author.username, b.author.firstName, b.author.lastName, b.author.avatar, b.reviewCount) FROM Book b LEFT JOIN b.bookSerie WHERE b.author.username = ?1 ORDER BY b.name")
    Page<BookResponse> findBooksByAuthor(final String authorId, final Pageable pageable);

    @Query("SELECT COUNT(b) FROM Book b")
    Long getBooksCount();

    @Query("SELECT new org.booklink.models.response.BookWithTextResponse(b.id, b.bookSerie.id, b.bookSerie.name, b.author.username, b.author.firstName, b.author.lastName, b.commentsCount, b.cover, b.created, b.description, b.genre, b.language, b.lastUpdate, b.name, LENGTH(b.bookText.text), b.totalRating, b.totalVotes, b.views, b.bookText.id, b.bookText.text, b.bookText.prevText) FROM Book b LEFT JOIN b.bookSerie WHERE b.id = ?1")
    BookWithTextResponse getBookById(final Long id);

    @Query("SELECT new org.booklink.models.response.BookResponse(b.id, b.bookSerie.id, b.bookSerie.name, b.commentsCount, b.cover, b.created, b.description, b.genre, b.language, b.lastUpdate, b.name, LENGTH(b.bookText.text), b.totalRating, b.totalVotes, b.views, b.author.username, b.author.firstName, b.author.lastName, b.author.avatar, b.reviewCount) FROM Book b LEFT JOIN b.bookSerie WHERE UPPER(b.name) LIKE CONCAT(UPPER(?1), '%') ORDER BY b.lastUpdate DESC")
    Page<BookResponse> findBooksByName(String name, Pageable pageable);

    @Query("SELECT new org.booklink.models.response.BookResponse(b.id, b.bookSerie.id, b.bookSerie.name, b.commentsCount, b.cover, b.created, b.description, b.genre, b.language, b.lastUpdate, b.name, LENGTH(b.bookText.text), b.totalRating, b.totalVotes, b.views, b.author.username, b.author.firstName, b.author.lastName, b.author.avatar, b.reviewCount) FROM Book b LEFT JOIN b.bookSerie ORDER BY b.lastUpdate")
    Page<BookResponse> getAllBooksSortedByDate(Pageable pageable);

    /* tops */
    @Query("SELECT new org.booklink.models.top_models.TopBookNovelties(b.id, b.name, b.lastUpdate) FROM Book b ORDER BY b.lastUpdate DESC")
    Page<TopBookNovelties> findAllByLastUpdate(Pageable pageable);

    //@Query("SELECT new org.booklink.models.top_models.TopBookRating(b.id, b.name, COALESCE(sum(r.ratingId.estimation), 0), count(r.ratingId.estimation)) FROM Book b LEFT JOIN b.rating r GROUP BY b.id ORDER BY COALESCE(sum(r.ratingId.estimation), 0) / (count(r.ratingId.estimation)+1) DESC")
    @Query("SELECT new org.booklink.models.top_models.TopBookRating(b.id, b.name, b.totalRating, b.totalVotes) FROM Book b WHERE b.totalVotes > 0 ORDER BY b.totalRating / b.totalVotes DESC")
    Page<TopBookRating> findAllByRating(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookVolume(b.id, b.name, COALESCE(length(t.text), 0) ) FROM Book b LEFT JOIN b.bookText t ORDER BY COALESCE(length(t.text), 0) DESC")
    Page<TopBookVolume> findAllByVolume(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookComments(b.id, b.name, b.commentsCount) FROM Book b ORDER BY b.commentsCount DESC")
    Page<TopBookComments> findAllByComments(Pageable pageable);

    @Query("SELECT new org.booklink.models.top_models.TopBookViews(b.id, b.name, b.views) FROM Book b ORDER BY b.views DESC")
    Page<TopBookViews> findAllByViews(Pageable pageable);
}
