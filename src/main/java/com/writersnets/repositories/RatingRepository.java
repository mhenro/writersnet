package com.writersnets.repositories;

import com.writersnets.models.entities.Rating;
import com.writersnets.models.entities.RatingId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 03.11.2017.
 */
public interface RatingRepository extends CrudRepository<Rating, RatingId> {
    @Query("SELECT r FROM Rating r WHERE r.ratingId.book.id = ?1 AND r.ratingId.clientIp = ?2")
    Rating findRatingByBookIdAndClientIp(Long bookId, String clientIp);
}
