package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request.ReviewRequest;
import org.booklink.models.response.ReviewResponse;
import org.booklink.services.ReviewService;
import org.booklink.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.booklink.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 04.01.2018.
 */
@RestController
public class ReviewController {
    private ReviewService reviewService;
    private SessionService sessionService;

    @Autowired
    public ReviewController(final ReviewService reviewService, final SessionService sessionService) {
        this.reviewService = reviewService;
        this.sessionService = sessionService;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "review", method = RequestMethod.POST)
    public ResponseEntity<?> saveReview(@RequestBody ReviewRequest reviewRequest) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        sessionService.updateSession(token);
        try {
            reviewService.saveReview(reviewRequest);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Review was saved");
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Bad credentials");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> userNotFound(ObjectNotFoundException e) {
        Response<String> response = new Response<>();
        response.setCode(5);
        response.setMessage("User not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
