package com.writersnets.repositories;

import com.writersnets.models.entities.Review;
import com.writersnets.models.entities.ReviewIP;
import com.writersnets.models.entities.ReviewIP_PK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 07.01.2018.
 */
public interface ReviewIPRepository extends CrudRepository<ReviewIP, ReviewIP_PK> {
    @Query("SELECT r FROM ReviewIP tmp LEFT JOIN tmp.reviewPK.review r WHERE tmp.reviewPK.review.id = ?1 AND tmp.reviewPK.ip = ?2")
    Review getReviewByIdAndIP(final Long id, final String ip);
}
