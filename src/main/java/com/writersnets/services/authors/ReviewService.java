package com.writersnets.services.authors;

import com.writersnets.models.entities.books.Book;
import com.writersnets.models.entities.books.Review;
import com.writersnets.models.entities.books.ReviewIP;
import com.writersnets.models.entities.books.ReviewIP_PK;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.exceptions.ObjectAlreadyExistException;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.request.ReviewRequest;
import com.writersnets.models.response.ReviewResponse;
import com.writersnets.repositories.AuthorRepository;
import com.writersnets.repositories.BookRepository;
import com.writersnets.repositories.ReviewIPRepository;
import com.writersnets.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mhenr on 04.01.2018.
 */
@Service
@Transactional(readOnly = true)
public class ReviewService {
    private ReviewRepository reviewRepository;
    private ReviewIPRepository reviewIPRepository;
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public ReviewService(final ReviewRepository reviewRepository, final ReviewIPRepository reviewIPRepository,
                         final BookRepository bookRepository, final AuthorRepository authorRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewIPRepository = reviewIPRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Page<ReviewResponse> getReviews(final Pageable pageable) {
        return reviewRepository.getReviews(pageable);
    }

    public Page<ReviewResponse> getReviewsByBookId(final Long bookId, final Pageable pageable) {
        return reviewRepository.getReviewsByBookId(bookId, pageable);
    }

    public ReviewResponse getReviewDetails(final Long reviewId) {
        return reviewRepository.getReviewDetails(reviewId);
    }

    @Transactional
    public long likeReview(final Long reviewId, final HttpServletRequest request) {
        final String clientIp = request.getHeader("X-Real-IP");
        final Review check = reviewIPRepository.getReviewByIdAndIP(reviewId, clientIp);
        if (check != null) {
            throw new ObjectAlreadyExistException("You've already liked this review");
        }
        final Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ObjectNotFoundException("Review is not found"));
        final long likes = review.getLikes() + 1;
        review.setLikes(likes);

        saveClientIp(review, clientIp);

        return likes;
    }

    @Transactional
    public long dislikeReview(final Long reviewId, final HttpServletRequest request) {
        final String clientIp = request.getHeader("X-Real-IP");
        final Review check = reviewIPRepository.getReviewByIdAndIP(reviewId, clientIp);
        if (check != null) {
            throw new ObjectAlreadyExistException("You've already disliked this review");
        }
        final Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ObjectNotFoundException("Review is not found"));
        final long dislikes = review.getDislikes() + 1;
        review.setDislikes(dislikes);

        saveClientIp(review, clientIp);

        return dislikes;
    }

    @Transactional
    public void saveReview(final ReviewRequest reviewRequest) {
        final User author = authorRepository.findById(reviewRequest.getAuthorId()).orElseThrow(() -> new ObjectNotFoundException("Author is not found"));
        if (reviewRequest.getId() != null) {    //updating review
            final Review review = reviewRepository.findById(reviewRequest.getId()).orElseThrow(() -> new ObjectNotFoundException("Review is not found"));
            review.setText(reviewRequest.getText());
            review.setAuthor(author);
            review.setName(reviewRequest.getName());
            review.setScore(reviewRequest.getScore());
            review.setLikes(0L);
            review.setDislikes(0L);
            reviewRepository.save(review);
            increaseReviewsInBook(review.getBook());
        } else {    //new review
            final Book book = bookRepository.findById(reviewRequest.getBookId()).orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
            final Review review = new Review();
            review.setText(reviewRequest.getText());
            review.setBook(book);
            review.setAuthor(author);
            review.setName(reviewRequest.getName());
            review.setScore(reviewRequest.getScore());
            review.setLikes(0L);
            review.setDislikes(0L);
            reviewRepository.save(review);
            increaseReviewsInBook(review.getBook());
        }
    }

    private void increaseReviewsInBook(final Book book) {
        final long reviewCount = book.getReviewCount() + 1;
        book.setReviewCount(reviewCount);
    }

    private void saveClientIp(final Review review, final String ip) {
        final ReviewIP reviewIP = new ReviewIP();
        final ReviewIP_PK reviewIP_pk = new ReviewIP_PK();
        reviewIP_pk.setIp(ip);
        reviewIP_pk.setReview(review);
        reviewIP.setReviewPK(reviewIP_pk);
        reviewIPRepository.save(reviewIP);
    }
}
