package org.booklink.services;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.Review;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.request.ReviewRequest;
import org.booklink.repositories.BookRepository;
import org.booklink.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ReviewService(final ReviewRepository reviewRepository, final BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void saveReview(final ReviewRequest reviewRequest) {
        if (reviewRequest.getId() != null) {    //updating review
            final Review review = reviewRepository.findOne(reviewRequest.getId());
            if (review == null) {
                throw new ObjectNotFoundException("Review was not found");
            }
            review.setText(reviewRequest.getText());
            reviewRepository.save(review);
        } else {    //new review
            final Book book = bookRepository.findOne(reviewRequest.getBookId());
            if (book == null) {
                throw new ObjectNotFoundException("Book was not found");
            }
            final Review review = new Review();
            review.setText(reviewRequest.getText());
            review.setBook(book);
            reviewRepository.save(review);
        }
    }
}
