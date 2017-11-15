package org.booklink.services;

import org.booklink.models.entities.Rating;
import org.booklink.models.entities.RatingId;
import org.booklink.models.exceptions.ObjectAlreadyExistException;
import org.booklink.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mhenr on 15.11.2017.
 */
@Service
public class RatingService {
    private RatingRepository ratingRepository;

    @Autowired
    public RatingService(final RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public void addStar(final Long bookId, final Integer value, final HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        Rating rating = ratingRepository.findRatingByBookIdAndClientIp(bookId, clientIp);
        if (rating != null) {
            throw new ObjectAlreadyExistException("You have already added your vote for this book");
        }
        rating = new Rating();
        RatingId ratingId = new RatingId();
        ratingId.setBookId(bookId);
        ratingId.setEstimation(value);
        ratingId.setClientIp(clientIp);
        rating.setRatingId(ratingId);
        ratingRepository.save(rating);
    }
}
