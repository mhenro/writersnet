package org.booklink.repositories;

import org.booklink.models.entities.Review;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 04.01.2018.
 */
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {
}
