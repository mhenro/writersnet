package com.writersnets.repositories;

import com.writersnets.models.entities.contests.ContestParticipant;
import com.writersnets.models.entities.contests.ContestParticipantPK;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.response.ContestUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by mhenr on 09.03.2018.
 */
public interface ContestParticipantRepository extends PagingAndSortingRepository<ContestParticipant, ContestParticipantPK> {
    @Query("SELECT c.pk.book.id FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    List<String> getParticipantBookIdFromContest(final Long id);

    @Query("SELECT c.pk.participant.username FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    List<String> getParticipantsIdFromContest(final Long id);

    @Query("SELECT new com.writersnets.models.response.ContestUserResponse(c.pk.participant.username, c.pk.participant.firstName, c.pk.participant.lastName, c.pk.contest.id, c.pk.contest.name, c.pk.book.id, c.pk.book.name, c.accepted) FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    Page<ContestUserResponse> getParticipantsFromContest(final Long id, final Pageable pageable);

    @Query("SELECT COUNT(*) FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    Long getParticipantCountFromContest(final Long id);

    @Query("SELECT COUNT(*) FROM ContestJudge c WHERE c.pk.contest.id = ?1 AND c.accepted = false")
    Long getNotAcceptedParticipantsFromContest(final Long id);

    @Query("SELECT COUNT(*) FROM ContestParticipant c WHERE c.pk.participant.username = ?1 AND c.accepted = false AND c.pk.contest.closed = false AND c.pk.contest.started = false")
    Long getNotAcceptedContestsByUser(final String userId);

    @Query("SELECT c.pk.participant FROM ContestParticipant c WHERE c.pk.contest.id = ?2 AND c.pk.participant.username = ?1")
    User getParticipantById(final String userId, final Long contestId);

    @Modifying
    @Query("UPDATE ContestParticipant p SET p.accepted = true WHERE p.pk.participant.username = ?1 AND p.pk.contest.id = ?2 AND p.pk.book.id = ?3")
    void joinInContest(final String userId, final Long contestId, final Long bookId);

    @Modifying
    @Query("UPDATE ContestParticipant p SET p.accepted = false WHERE p.pk.participant.username = ?1 AND p.pk.contest.id = ?2 AND p.pk.book.id = ?3")
    void refuseContest(final String userId, final Long contestId, final Long bookId);

    @Modifying
    @Query("DELETE FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    void clearParticipantsInContest(final Long id);
}
