package com.writersnets.repositories;

import com.writersnets.models.entities.contests.ContestRating;
import com.writersnets.models.entities.contests.ContestRatingPK;
import com.writersnets.models.response.ContestRatingDetailsResponse;
import com.writersnets.models.response.ContestRatingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ContestRatingRepository extends CrudRepository<ContestRating, ContestRatingPK> {
    @Query("SELECT new com.writersnets.models.response.ContestRatingResponse(r.pk.book.id, r.pk.book.name, AVG(r.estimation)) FROM ContestRating r WHERE r.pk.contest.id = ?1 GROUP BY r.pk.book.id, r.pk.book.name ORDER BY AVG(r.estimation) DESC")
    Page<ContestRatingResponse> getParticipantsRating(final long contestId, final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.ContestRatingDetailsResponse(r.pk.judge.username, r.pk.judge.firstName, r.pk.judge.lastName, r.estimation) FROM ContestRating r WHERE r.pk.contest.id = ?1 AND r.pk.book.id = ?2 ORDER BY r.estimation DESC")
    Page<ContestRatingDetailsResponse> getParticipantsRatingDetails(final long contestId, final long bookId, final Pageable pageable);
}
