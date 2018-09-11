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
    @Query("SELECT new com.writersnets.models.response.ContestRatingResponse(p.pk.book.id, p.pk.book.name, COALESCE(AVG(r.estimation), 0.0), p.pk.participant.username) " +
            "FROM ContestParticipant p " +
            "LEFT JOIN ContestRating r ON p.pk.book.id = r.pk.book.id " +
            "WHERE p.pk.contest.id = ?1 " +
            "GROUP BY p.pk.book.id, p.pk.book.name, p.pk.participant.username " +
            "ORDER BY AVG(r.estimation) DESC")
    Page<ContestRatingResponse> getParticipantsRating(final long contestId, final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.ContestRatingDetailsResponse(r.pk.judge.username, r.pk.judge.firstName, r.pk.judge.lastName, r.estimation) " +
            "FROM ContestRating r " +
            "WHERE r.pk.contest.id = ?1 AND r.pk.book.id = ?2 " +
            "ORDER BY r.estimation DESC")
    Page<ContestRatingDetailsResponse> getParticipantsRatingDetails(final long contestId, final long bookId, final Pageable pageable);

    @Query("SELECT COUNT(*) FROM ContestRating r WHERE r.pk.contest.id = ?1")
    Integer getEstimationCount(final long contestId);
}
