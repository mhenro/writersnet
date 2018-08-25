package com.writersnets.services.authors;

import com.writersnets.models.entities.books.Book;
import com.writersnets.models.entities.books.Rating;
import com.writersnets.models.entities.books.RatingId;
import com.writersnets.models.exceptions.ObjectAlreadyExistException;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.repositories.BookRepository;
import com.writersnets.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mhenr on 15.11.2017.
 */
@Service
@Transactional
public class RatingService {
    private RatingRepository ratingRepository;
    private BookRepository bookRepository;

    @Autowired
    public RatingService(final RatingRepository ratingRepository, final BookRepository bookRepository) {
        this.ratingRepository = ratingRepository;
        this.bookRepository = bookRepository;
    }

    public void addStar(final Long bookId, final Integer value, final HttpServletRequest request) {
        final String clientIp = request.getHeader("X-Real-IP");
        Rating rating = ratingRepository.findRatingByBookIdAndClientIp(bookId, clientIp);
        if (rating != null) {
            throw new ObjectAlreadyExistException("You have already added your vote for this book");
        }
        final Book book = bookRepository.findById(bookId).orElseThrow(() -> new ObjectNotFoundException("Book was not found"));
        rating = new Rating();
        RatingId ratingId = new RatingId();
        ratingId.setBook(book);
        ratingId.setEstimation(value);
        ratingId.setClientIp(clientIp);
        rating.setRatingId(ratingId);
        updateRating(book, value);
        book.getAuthor().refreshRatingFromBooks();
        ratingRepository.save(rating);
    }

    public void updateRating(final Book book, final Integer estimation) {
        final Long totalRating = book.getTotalRating() + estimation;
        final Long totalVotes = book.getTotalVotes() + 1;
        book.setTotalRating(totalRating);
        book.setTotalVotes(totalVotes);
    }
}
