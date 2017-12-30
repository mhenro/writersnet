package org.booklink.services;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.Rating;
import org.booklink.models.entities.RatingId;
import org.booklink.models.exceptions.ObjectAlreadyExistException;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.BookRepository;
import org.booklink.repositories.RatingRepository;
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
        final Book book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new ObjectNotFoundException("Book was not found");
        }
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
