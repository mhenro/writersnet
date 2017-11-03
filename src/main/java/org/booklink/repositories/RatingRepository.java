package org.booklink.repositories;

import org.booklink.models.entities.Rating;
import org.booklink.models.entities.RatingId;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 03.11.2017.
 */
public interface RatingRepository extends CrudRepository<Rating, RatingId> {
}
