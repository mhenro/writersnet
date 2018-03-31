package org.booklink.repositories;

import org.booklink.models.entities.ContestParticipant;
import org.booklink.models.entities.ContestParticipantPK;
import org.booklink.models.entities.User;
import org.booklink.models.response.ContestUserResponse;
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

    @Query("SELECT new org.booklink.models.response.ContestUserResponse(c.pk.participant.username, c.pk.participant.firstName, c.pk.participant.lastName, c.pk.contest.id, c.pk.contest.name, c.pk.book.id, c.pk.book.name, c.accepted) FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
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
    @Query("UPDATE ContestParticipant p SET p.accepted = true WHERE p.pk.participant.username = ?1 AND p.pk.contest.id = ?2")
    void joinInContest(final String userId, final Long contestId);

    @Modifying
    @Query("DELETE FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    void clearParticipantsInContest(final Long id);
}
