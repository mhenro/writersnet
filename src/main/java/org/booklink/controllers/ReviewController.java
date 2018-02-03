package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.exceptions.ObjectAlreadyExistException;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request.ReviewRequest;
import org.booklink.models.response.ReviewResponse;
import org.booklink.services.ReviewService;
import org.booklink.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.booklink.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 04.01.2018.
 */
@RestController
public class ReviewController {
    private ReviewService reviewService;
    private SessionService sessionService;
    private Environment environment;

    @Autowired
    public ReviewController(final ReviewService reviewService, final SessionService sessionService, final Environment environment) {
        this.reviewService = reviewService;
        this.sessionService = sessionService;
        this.environment = environment;
    }

    @CrossOrigin
    @RequestMapping(value = "reviews", method = RequestMethod.GET)
    public Page<ReviewResponse> getReviews(final Pageable pageable) {
        return reviewService.getReviews(pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "reviews/{bookId}", method = RequestMethod.GET)
    public Page<ReviewResponse> getReviews(@PathVariable final Long bookId, final Pageable pageable) {
        return reviewService.getReviewsByBookId(bookId, pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "review/{reviewId}", method = RequestMethod.GET)
    public ResponseEntity<?> getReviewDetails(@PathVariable final Long reviewId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        final ReviewResponse review = reviewService.getReviewDetails(reviewId);
        return Response.createResponseEntity(0, review, token, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "review/{reviewId}/like", method = RequestMethod.GET)
    public ResponseEntity<?> likeReview(@PathVariable final Long reviewId, final HttpServletRequest request) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        long likes = reviewService.likeReview(reviewId, request);
        return Response.createResponseEntity(0, likes, token, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "review/{reviewId}/dislike", method = RequestMethod.GET)
    public ResponseEntity<?> dislikeReview(@PathVariable final Long reviewId, final HttpServletRequest request) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        long dislikes = reviewService.dislikeReview(reviewId, request);
        return Response.createResponseEntity(0, dislikes, token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "review", method = RequestMethod.POST)
    public ResponseEntity<?> saveReview(@RequestBody final ReviewRequest reviewRequest) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        reviewService.saveReview(reviewRequest);
        return Response.createResponseEntity(0, "Review was saved", token, HttpStatus.OK);
    }

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, e.getMessage().isEmpty() ? "Bad credentials" : e.getMessage(), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> objectNotFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(5, e.getMessage().isEmpty() ? "Object is not found" : e.getMessage(), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObjectAlreadyExistException.class)
    public ResponseEntity<?> objectAlreadyExist(ObjectAlreadyExistException e) {
        return Response.createResponseEntity(5, e.getMessage().isEmpty() ? "Object already exist" : e.getMessage(), null, HttpStatus.BAD_REQUEST);
    }
}
