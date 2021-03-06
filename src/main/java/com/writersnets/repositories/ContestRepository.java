package com.writersnets.repositories;

import com.writersnets.models.entities.contests.Contest;
import com.writersnets.models.response.ContestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 25.02.2018.
 */
public interface ContestRepository extends PagingAndSortingRepository<Contest, Long> {
    @Query("SELECT new com.writersnets.models.response.ContestResponse(c.id, c.name, c.creator.username, c.creator.firstName, " +
            "c.creator.lastName, c.prizeFund, c.firstPlaceRevenue, c.secondPlaceRevenue, c.thirdPlaceRevenue, c.created, " +
            "c.expirationDate, c.started, c.closed, c.participantCount, c.judgeCount) FROM Contest c ORDER BY c.closed, c.created DESC")
    Page<ContestResponse> getAllContests(final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.ContestResponse(c.id, c.name, c.creator.username, c.creator.firstName, " +
            "c.creator.lastName, p.pk.book.id, p.pk.book.name, c.prizeFund, c.firstPlaceRevenue, c.secondPlaceRevenue, c.thirdPlaceRevenue, c.created, " +
            "c.expirationDate, c.started, c.closed, p.accepted, c.participantCount, c.judgeCount) FROM ContestParticipant p JOIN p.pk.contest c WHERE p.pk.participant.username = ?1 ORDER BY c.closed, c.started, c.created DESC")
    Page<ContestResponse> getParticipantContests(final String userId, final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.ContestResponse(c.id, c.name, c.creator.username, c.creator.firstName, " +
            "c.creator.lastName, -1L, '', c.prizeFund, c.firstPlaceRevenue, c.secondPlaceRevenue, c.thirdPlaceRevenue, c.created, " +
            "c.expirationDate, c.started, c.closed, j.accepted, c.participantCount, c.judgeCount) FROM ContestJudge j JOIN j.pk.contest c WHERE j.pk.judge.username = ?1 ORDER BY c.closed, c.started, c.created DESC")
    Page<ContestResponse> getJudgeContests(final String userId, final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.ContestResponse(c.id, c.name, c.creator.username, c.creator.firstName, " +
            "c.creator.lastName, c.prizeFund, c.firstPlaceRevenue, c.secondPlaceRevenue, c.thirdPlaceRevenue, c.created, " +
            "c.expirationDate, c.started, c.closed, c.participantCount, c.judgeCount) FROM Contest c WHERE c.creator.username = ?1 ORDER BY c.closed, c.started, c.created DESC")
    Page<ContestResponse> getCreatorContests(final String creatorId, final Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.ContestResponse(c.id, c.name, c.creator.username, c.creator.firstName, " +
            "c.creator.lastName, c.prizeFund, c.firstPlaceRevenue, c.secondPlaceRevenue, c.thirdPlaceRevenue, c.created, " +
            "c.expirationDate, c.started, c.closed, c.participantCount, c.judgeCount) FROM Contest c WHERE c.id = ?1")
    ContestResponse getContest(final Long id);

    @Query("SELECT c FROM Contest c WHERE c.expirationDate < ?1 AND c.closed = false AND c.started = true")
    Page<Contest> getExpiredContests(final LocalDateTime currentDate, final Pageable pageable);
}