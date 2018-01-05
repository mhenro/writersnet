package org.booklink.services;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.Review;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.request.ReviewRequest;
import org.booklink.models.response.ReviewResponse;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.BookRepository;
import org.booklink.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mhenr on 04.01.2018.
 */
@Service
@Transactional(readOnly = true)
public class ReviewService {
    private ReviewRepository reviewRepository;
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public ReviewService(final ReviewRepository reviewRepository, final BookRepository bookRepository, final AuthorRepository authorRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Page<ReviewResponse> getReviews(final Pageable pageable) {
        return reviewRepository.getReviews(pageable);
    }

    public Page<ReviewResponse> getReviewsByBookId(final Long bookId, final Pageable pageable) {
        return reviewRepository.getReviewsByBookId(bookId, pageable);
    }

    @Transactional
    public void saveReview(final ReviewRequest reviewRequest) {
        final User author = authorRepository.findOne(reviewRequest.getAuthorId());
        if (author == null) {
            throw new ObjectNotFoundException("Author was not found");
        }
        if (reviewRequest.getId() != null) {    //updating review
            final Review review = reviewRepository.findOne(reviewRequest.getId());
            if (review == null) {
                throw new ObjectNotFoundException("Review was not found");
            }
            review.setText(reviewRequest.getText());
            review.setAuthor(author);
            review.setName(reviewRequest.getName());
            review.setScore(reviewRequest.getScore());
            reviewRepository.save(review);
            increaseReviewsInBook(review.getBook());
        } else {    //new review
            final Book book = bookRepository.findOne(reviewRequest.getBookId());
            if (book == null) {
                throw new ObjectNotFoundException("Book was not found");
            }
            final Review review = new Review();
            review.setText(reviewRequest.getText());
            review.setBook(book);
            review.setAuthor(author);
            review.setName(reviewRequest.getName());
            review.setScore(reviewRequest.getScore());
            reviewRepository.save(review);
            increaseReviewsInBook(review.getBook());
        }
    }

    private void increaseReviewsInBook(final Book book) {
        final long reviewCount = book.getReviewCount() + 1;
        book.setReviewCount(reviewCount);
    }
}
